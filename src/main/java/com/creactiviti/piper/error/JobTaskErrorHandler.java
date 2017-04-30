/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.error;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.event.Events;
import com.creactiviti.piper.core.event.PiperEvent;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.job.JobStatus;
import com.creactiviti.piper.core.job.MutableJob;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.JobTaskRepository;
import com.creactiviti.piper.core.task.TaskDispatcher;
import com.creactiviti.piper.core.task.TaskStatus;

/**
 * 
 * @author Arik Cohen
 * @since Apr 10, 2017
 */
public class JobTaskErrorHandler implements ErrorHandler<JobTask> {

  private JobRepository jobRepository;
  private JobTaskRepository jobTaskRepository;
  private TaskDispatcher taskDispatcher;
  private ApplicationEventPublisher eventPublisher;

  private Logger logger = LoggerFactory.getLogger(getClass());
    
  @Override
  public void handle(JobTask aTask) {
    Error error = aTask.getError();
    Assert.notNull(error,"error must not be null");
    logger.debug("Erring task {}: {}\n{}", aTask.getId(), error.getMessage());
    
    // set task status to failed and persist
    MutableJobTask mtask = MutableJobTask.createForUpdate(aTask);
    mtask.setStatus(TaskStatus.FAILED);
    jobTaskRepository.update(mtask);
    
    // if the task is retryable, then retry it
    if(aTask.getRetryAttempts() < aTask.getRetry()) {
      MutableJobTask retryTask = MutableJobTask.createNewFrom(aTask);
      retryTask.setRetryAttempts(aTask.getRetryAttempts()+1);
      jobTaskRepository.create(retryTask);
      taskDispatcher.dispatch(retryTask);
    }
    // if it's not retryable then we're gonna fail the job
    else {
      while(mtask.getParentId()!=null) { // mark parent tasks as FAILED as well
        mtask = MutableJobTask.createForUpdate(jobTaskRepository.findOne(mtask.getParentId()));
        mtask.setStatus(TaskStatus.FAILED);
        jobTaskRepository.update(mtask);
      }
      Job job = jobRepository.findJobByTaskId(mtask.getId());
      Assert.notNull(job,"job not found for task: " + mtask.getId());
      MutableJob mjob = new MutableJob (job);
      Assert.notNull(mjob,String.format("No job found for task %s ",mtask.getId()));
      mjob.setStatus(JobStatus.FAILED);
      mjob.setEndTime(new Date ());
      jobRepository.update(mjob);
      eventPublisher.publishEvent(PiperEvent.of(Events.JOB_STATUS, "jobId", mjob.getId(), "status", mjob.getStatus()));
    }
  }

  public void setJobRepository(JobRepository aJobRepository) {
    jobRepository = aJobRepository;
  }

  public void setJobTaskRepository(JobTaskRepository aJobTaskRepository) {
    jobTaskRepository = aJobTaskRepository;
  }

  public void setTaskDispatcher(TaskDispatcher aTaskDispatcher) {
    taskDispatcher = aTaskDispatcher;
  }
  
  public void setEventPublisher(ApplicationEventPublisher aEventPublisher) {
    eventPublisher = aEventPublisher;
  }

}
