/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
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
import com.creactiviti.piper.core.task.CancelTask;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.JobTaskRepository;
import com.creactiviti.piper.core.task.NoOpTaskEvaluator;
import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskEvaluator;
import com.creactiviti.piper.core.task.TaskExecutor;
import com.creactiviti.piper.core.task.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDGenerator;
import com.creactiviti.piper.error.ErrorHandler;
import com.creactiviti.piper.error.Errorable;

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
  private JobTaskRepository jobTaskRepository;
  private ApplicationEventPublisher eventPublisher;
  private ContextRepository contextRepository;
  private TaskExecutor taskExecutor;
  private TaskEvaluator taskEvaluator = new NoOpTaskEvaluator();
  private ErrorHandler errorHandler;

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
  public Job start (Map<String,Object> aParameters) {
    Assert.notNull(aParameters,"parameters can't be null");
    MapObject params = MapObject.of(aParameters);
    String pipelineId = params.getRequiredString(PIPELINE);
    Pipeline pipeline = pipelineRepository.findOne(pipelineId);    
    Assert.notNull(pipeline,String.format("Unkown pipeline: %s", pipelineId));

    validate(params, pipeline);

    MutableJob job = new MutableJob();
    job.setId(UUIDGenerator.generate());
    job.setName(params.getString("name",pipeline.getName()));
    job.setPipelineId(pipeline.getId());
    job.setStatus(JobStatus.STARTED);
    job.setStartDate(new Date());
    job.setCreationDate(new Date());
    log.debug("Job {} started",job.getId());
    jobRepository.create(job);

    MapContext context = new MapContext(params);
    context.setId(UUIDGenerator.generate());
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
    if(aJob.getStatus() != JobStatus.STARTED) {
      return;
    }
    else if(hasMoreTasks(aJob, aPipeline)) {
      executeNextTask (aJob, aPipeline);
    }
    else {
      completeJob(aJob);
    }
  }

  private boolean hasMoreTasks (Job aJob, Pipeline aPipeline) {
    return aJob.getCurrentTask()+1 < aPipeline.getTasks().size();
  }

  private JobTask nextTask(Job aJob, Pipeline aPipeline) {
    Task task = aPipeline.getTasks().get(aJob.getCurrentTask()+1);
    MutableJobTask mt = new MutableJobTask (task);
    mt.setJobId(aJob.getId());
    mt.setStatus(TaskStatus.CREATED);
    jobTaskRepository.create(mt);
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
    contextRepository.pop(aJob.getId());
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
    Job job = jobRepository.findOne(aJobId);
    Assert.notNull(job,"Unknown job: " + aJobId);
    Assert.isTrue(job.getStatus()==JobStatus.STARTED,"Job " + aJobId + " can not be stopped as it is " + job.getStatus());
    MutableJob mjob = new MutableJob(job);
    mjob.setStatus(JobStatus.STOPPED);
    jobRepository.update(mjob);
    if(mjob.getExecution().size() > 0) {
      MutableJobTask currentTask = new MutableJobTask(job.getExecution().get(job.getExecution().size()-1));
      currentTask.setStatus(TaskStatus.CANCELLED);
      currentTask.setCancellationDate(new Date());
      jobTaskRepository.update(currentTask);
      taskExecutor.execute(new CancelTask(currentTask.getId()));
    }
    return mjob;
  }

  /**
   * Resume a stopped or failed job.
   * 
   * @param aJobId
   *          The id of the job to resume.
   * @return The resumed job
   */
  public Job resume (String aJobId) {
    log.debug("Resuming job {}", aJobId);
    Job job = jobRepository.findOne (aJobId);
    Assert.notNull(job,String.format("Unknown job %s",aJobId));
    Assert.isTrue(isRestartable(job), "can't stop job " + aJobId + " as it is " + job.getStatus());
    Pipeline pipeline = pipelineRepository.findOne(job.getPipelineId());
    MutableJob mjob = new MutableJob (job);
    mjob.setStatus(JobStatus.STARTED);
    jobRepository.update(mjob);
    execute(mjob,pipeline);
    return mjob;
  }

  private boolean isRestartable (Job aJob) {
    return aJob.getStatus() == JobStatus.STOPPED || aJob.getStatus() == JobStatus.FAILED;
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
    task.setError(null);
    Job job = jobRepository.findJobByTaskId (aTask.getId());
    if(job!=null) {
      Pipeline pipeline = pipelineRepository.findOne(job.getPipelineId());
      MutableJob mjob = new MutableJob (job);
      mjob.setCurrentTask(mjob.getCurrentTask()+1);
      jobTaskRepository.update(task);
      jobRepository.update(mjob);
      if(task.getOutput() != null && task.getName() != null) {
        Context context = contextRepository.pop(job.getId());
        MapContext newContext = new MapContext(context.asMap());
        newContext.setId(UUIDGenerator.generate());
        newContext.put(task.getName(), task.getOutput());
        contextRepository.push(job.getId(), newContext);
      }
      execute(mjob,pipeline);
    }
    else {
      log.error("Unknown job: {}",aTask.getJobId());
    }
  }

  /**
   * Handle an application error.
   * 
   * @param aErrorable
   *          The erring message.
   */
  public void handleError (Errorable aErrorable) {
    errorHandler.handle(aErrorable);
  }

  /**
   * Receives application events generated by the application
   * nodes and distributes them to captured by {@link ApplicationListener}
   * implementations.
   * 
   * @param aEvent
   *          The event received
   */
  public void on (Object aEvent) {
    log.debug("Received event {}",aEvent);
    eventPublisher.publishEvent(aEvent);
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

  public void setJobTaskRepository(JobTaskRepository aJobTaskRepository) {
    jobTaskRepository = aJobTaskRepository;
  }

  public void setErrorHandler(ErrorHandler aErrorHandler) {
    errorHandler = aErrorHandler;
  }
  
}
