/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.context.Context;
import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.context.MapContext;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.job.JobStatus;
import com.creactiviti.piper.core.job.MutableJob;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.pipeline.PipelineRepository;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.NoOpTaskEvaluator;
import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskEvaluator;
import com.creactiviti.piper.core.task.TaskExecutor;
import com.creactiviti.piper.core.task.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDGenerator;

/**
 * The central class responsible for coordinating 
 * and executing jobs.
 * 
 * @author Arik Cohen
 * @since Jun 12, 2016
 */
public class Coordinator {
  
  private PipelineRepository pipelineRepository;
  private JobRepository jobRepository;
  private ApplicationEventPublisher eventPublisher;
  private ContextRepository contextRepository;
  private TaskExecutor taskExecutor;
  private TaskEvaluator taskEvaluator = new NoOpTaskEvaluator();
  
  private static final String PIPELINE = "pipeline";
  
  private final Logger log = LoggerFactory.getLogger(getClass());
  
  /**
   * Starts a job instance.
   * 
   * @param aParameters
   *          The Key-Value map representing the job
   *          parameters
   * @return Job
   *           The instance of the Job
   */
  public Job start (MapObject aParameters) {
    Assert.notNull(aParameters,"parameters can't be null");
    String pipelineId = aParameters.getRequiredString(PIPELINE);
    Pipeline pipeline = pipelineRepository.findOne(pipelineId);    
    Assert.notNull(pipeline,String.format("Unkown pipeline: %s", pipelineId));
    
    validate(aParameters, pipeline);

    MutableJob job = new MutableJob();
    job.setId(UUIDGenerator.generate());
    job.setName(aParameters.getString("name",pipeline.getName()));
    job.setPipelineId(pipeline.getId());
    job.setStatus(JobStatus.STARTED);
    job.setStartDate(new Date());
    job.setCreationDate(new Date());
    log.debug("Job {} started",job.getId());
    jobRepository.create(job);
    
    Context context = new MapContext(aParameters);
    contextRepository.push(job.getId(),context);
    
    execute (job, pipeline);
    
    return job;
  }
  
  private void validate (MapObject aParameters, Pipeline aPipeline) {
    List<Accessor> input = aPipeline.getInputs();
    for(Accessor in : input) {
      if(in.getBoolean("required", false)) {
        Assert.isTrue(aParameters.containsKey(in.get("name")), "Missing required param: " + in.get("name"));
      }
    }
  }
  
  private void execute (MutableJob aJob, Pipeline aPipeline) {
    if(hasMoreTasks(aJob, aPipeline)) {
      executeNextTask (aJob, aPipeline);
    }
    else {
      completeJob(aJob);
    }
  }
  
  private boolean hasMoreTasks (Job aJob, Pipeline aPipeline) {
    return aJob.getCurrentTask()+1 < aPipeline.getTasks().size();
  }
  
  private JobTask nextTask(MutableJob aJob, Pipeline aPipeline) {
    aJob.setCurrentTask(aJob.getCurrentTask()+1);
    jobRepository.update(aJob);
    Task task = aPipeline.getTasks().get(aJob.getCurrentTask());
    MutableJobTask mt = new MutableJobTask (task);
    mt.setJobId(aJob.getId());
    jobRepository.create(mt);
    return mt;
  }

  private void executeNextTask (MutableJob aJob, Pipeline aPipeline) {
    JobTask nextTask = nextTask(aJob, aPipeline); 
    jobRepository.update(aJob);
    Context context = contextRepository.peek(aJob.getId());
    JobTask evaluatedTask = taskEvaluator.evaluate(nextTask,context);
    taskExecutor.execute(evaluatedTask);
  }
  
  private void completeJob (MutableJob aJob) {
    MutableJob job = new MutableJob((Job)aJob);
    job.setStatus(JobStatus.COMPLETED);
    job.setCompletionDate(new Date ());
    jobRepository.update(job);
    log.debug("Job {} completed successfully",aJob.getId());
  }

  /**
   * Stop a running job.
   * 
   * @param aJobId
   *          The id of the job to stop
   *          
   * @return The stopped {@link Job}
   */
  public Job stop (String aJobId) {
    throw new UnsupportedOperationException();
  }

  /**
   * Resume a stopped or failed job.
   * 
   * @param aJobId
   *          The id of the job to resume.
   * @return The resumed job
   */
  public Job resume (String aJobId) {
    throw new UnsupportedOperationException();
  }

  /**
   * Complete a task of a given job.
   * 
   * @param aTask
   *          The task to complete.
   */
  public void completeTask (JobTask aTask) {
    log.debug("Completing task {}", aTask.getId());
    MutableJobTask task = new MutableJobTask(aTask);
    task.setStatus(TaskStatus.COMPLETED);
    Job job = jobRepository.findJobByTaskId (aTask.getId());
    Pipeline pipeline = pipelineRepository.findOne(job.getPipelineId());
    MutableJob mjob = new MutableJob (job);
    Assert.notNull(mjob,String.format("No job found for task %s ",aTask.getId()));
    jobRepository.update(task);
    jobRepository.update(mjob);
    
    if(task.getOutput() != null) {
      Context context = contextRepository.pop(job.getId());
      MapContext newContext = new MapContext(context.asMap());
      newContext.put(task.getName(), task.getOutput());
      contextRepository.push(job.getId(), newContext);
    }
    
    execute(mjob,pipeline);
  }

  /**
   * Handle an erroring task.
   * 
   * @param aTask
   *          The task to handle.
   */
  public void error (JobTask aTask) {
    log.debug("Erroring task {}", aTask);
    MutableJobTask task = new MutableJobTask(aTask);
    task.setStatus(TaskStatus.FAILED);
    Job job = jobRepository.findJobByTaskId (aTask.getId());
    MutableJob mjob = new MutableJob (job);
    Assert.notNull(mjob,String.format("No job found for task %s ",aTask.getId()));
    mjob.setStatus(JobStatus.FAILED);
    mjob.setFailedDate(new Date ());
    jobRepository.update(task);
    jobRepository.update(mjob);
  }

  /**
   * Handles application events. 
   * 
   * @param aEvent
   *          The event to handle
   */
  public void on (Object aEvent) {
    eventPublisher.publishEvent (aEvent);    
  }
 
  public void setContextRepository(ContextRepository aContextRepository) {
    contextRepository = aContextRepository;
  }

  public void setEventPublisher(ApplicationEventPublisher aEventPublisher) {
    eventPublisher = aEventPublisher;
  }
  
  public void setJobRepository(JobRepository aJobRepository) {
    jobRepository = aJobRepository;
  }

  public void setTaskExecutor(TaskExecutor aTaskExecutor) {
    taskExecutor = aTaskExecutor;
  }
  
  public void setPipelineRepository(PipelineRepository aPipelineRepository) {
    pipelineRepository = aPipelineRepository;
  }
  
  public void setTaskEvaluator(TaskEvaluator aTaskEvaluator) {
    taskEvaluator = aTaskEvaluator;
  }
  

}
