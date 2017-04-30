/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import com.creactiviti.piper.core.context.Context;
import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.context.MapContext;
import com.creactiviti.piper.core.event.Events;
import com.creactiviti.piper.core.event.PiperEvent;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.job.JobStatus;
import com.creactiviti.piper.core.job.MutableJob;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.pipeline.PipelineRepository;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.JobTaskRepository;
import com.creactiviti.piper.core.task.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDGenerator;


/**
 * 
 * @author Arik Cohen
 * @since Apr 24, 2017
 */
public class DefaultTaskCompletionHandler implements TaskCompletionHandler {

  private Logger log = LoggerFactory.getLogger(getClass());
  
  private JobRepository jobRepository;
  private PipelineRepository pipelineRepository;
  private JobTaskRepository jobTaskRepository;
  private ContextRepository contextRepository;
  private JobExecutor jobExecutor;
  private ApplicationEventPublisher eventPublisher;
  
  @Override
  public void handle (JobTask aTask) {
    log.debug("Completing task {}", aTask.getId());
    MutableJobTask task = MutableJobTask.createForUpdate(aTask);
    task.setStatus(TaskStatus.COMPLETED);
    task.setError(null);
    Job job = jobRepository.findJobByTaskId (aTask.getId());
    if(job!=null) {
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
      if(hasMoreTasks(mjob)) {
        jobExecutor.execute(mjob);
      }
      else {
        complete(mjob);
      }
    }
    else {
      log.error("Unknown job: {}",aTask.getJobId());
    }
  }

  private boolean hasMoreTasks (Job aJob) {
    Pipeline pipeline = pipelineRepository.findOne(aJob.getPipelineId());
    return aJob.getCurrentTask()+1 < pipeline.getTasks().size();
  }

  private void complete (MutableJob aJob) {
    contextRepository.pop(aJob.getId());
    MutableJob job = new MutableJob((Job)aJob);
    job.setStatus(JobStatus.COMPLETED);
    job.setEndTime(new Date ());
    jobRepository.update(job);
    eventPublisher.publishEvent(PiperEvent.of(Events.JOB_STATUS, "jobId", aJob.getId(), "status", aJob.getStatus()));
    int stackSize = contextRepository.stackSize(aJob.getId());
    log.debug("Job {} completed successfully, stack size: {}",aJob.getId(),stackSize);
  }
  
  public void setJobRepository(JobRepository aJobRepository) {
    jobRepository = aJobRepository;
  }
  
  public void setPipelineRepository(PipelineRepository aPipelineRepository) {
    pipelineRepository = aPipelineRepository;
  }

  public void setJobTaskRepository(JobTaskRepository aJobTaskRepository) {
    jobTaskRepository = aJobTaskRepository;
  }
  
  public void setContextRepository(ContextRepository aContextRepository) {
    contextRepository = aContextRepository;
  }
  
  public void setJobExecutor(JobExecutor aJobExecutor) {
    jobExecutor = aJobExecutor;
  }
  
  public void setEventPublisher(ApplicationEventPublisher aEventPublisher) {
    eventPublisher = aEventPublisher;
  }

  @Override
  public boolean canHandle(JobTask aJobTask) {
    return aJobTask.getParentId()==null;
  }

}
