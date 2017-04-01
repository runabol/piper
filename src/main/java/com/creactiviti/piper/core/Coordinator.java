/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core;

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
import com.creactiviti.piper.core.task.TaskEvaluator;
import com.creactiviti.piper.core.task.TaskExecutor;
import com.creactiviti.piper.core.task.TaskStatus;

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

    MutableJob job = new MutableJob(pipeline);
    job.setStatus(JobStatus.STARTED);
    log.debug("Job {} started",job.getId());
    jobRepository.save(job);
    
    Context context = new MapContext(aParameters);
    contextRepository.push(job.getId(),context);
    
    execute (job);
    
    return job;
  }
  
  private void validate (MapObject aParameters, Pipeline aPipeline) {
    List<Accessor> input = aPipeline.getInput();
    for(Accessor in : input) {
      if(in.getBoolean("required", false)) {
        Assert.isTrue(aParameters.containsKey(in.get("name")), "Missing required param: " + in.get("name"));
      }
    }
  }
  
  private void execute (MutableJob aJob) {
    if(aJob.hasMoreTasks()) {
      executeNextTask (aJob);
    }
    else {
      completeJob(aJob);
    }
  }

  private void executeNextTask (MutableJob aJob) {
    JobTask nextTask = aJob.nextTask(); 
    jobRepository.save(aJob);
    Context context = contextRepository.peek(aJob.getId());
    JobTask evaluatedTask = taskEvaluator.evaluate(nextTask,context);
    taskExecutor.execute(evaluatedTask);
  }
  
  private void completeJob (MutableJob aJob) {
    Pipeline pipeline = pipelineRepository.findOne(aJob.getPipelineId());
    MutableJob job = new MutableJob(aJob,pipeline);
    job.setStatus(JobStatus.COMPLETED);
    jobRepository.save(job);
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
    MutableJob mjob = new MutableJob (job,pipeline);
    Assert.notNull(mjob,String.format("No job found for task %s ",aTask.getId()));
    mjob.updateTask(task);
    jobRepository.save(mjob);
    
    if(task.getOutput() != null) {
      Context context = contextRepository.pop(job.getId());
      MapContext newContext = new MapContext(context.asMap());
      newContext.put(task.getName(), task.getOutput());
      contextRepository.push(job.getId(), newContext);
    }
    
    execute(mjob);
  }

  /**
   * Handle an erroring task.
   * 
   * @param aTask
   *          The task to handle.
   */
  public void error (JobTask aTask) {
    log.debug("Completing task {}", aTask.getId());
    MutableJobTask task = new MutableJobTask(aTask);
    task.setStatus(TaskStatus.FAILED);
    Job job = jobRepository.findJobByTaskId (aTask.getId());
    Pipeline pipeline = pipelineRepository.findOne(job.getPipelineId());
    MutableJob mjob = new MutableJob (job,pipeline);
    Assert.notNull(mjob,String.format("No job found for task %s ",aTask.getId()));
    mjob.setStatus(JobStatus.FAILED);
    mjob.updateTask(task);
    jobRepository.save(mjob);
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
