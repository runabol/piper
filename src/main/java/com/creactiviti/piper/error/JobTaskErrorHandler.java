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
import org.springframework.util.Assert;

import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.job.JobStatus;
import com.creactiviti.piper.core.job.MutableJob;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.JobTaskRepository;
import com.creactiviti.piper.core.task.TaskExecutor;
import com.creactiviti.piper.core.task.TaskStatus;

/**
 * 
 * @author Arik Cohen
 * @since Apr 10, 2017
 */
public class JobTaskErrorHandler extends AbstractErrorHandler {

  private JobRepository jobRepository;
  private JobTaskRepository jobTaskRepository;
  private TaskExecutor taskExecutor;

  private Logger logger = LoggerFactory.getLogger(getClass());
    
  @Override
  protected void handleInternal(Errorable aErrorable) {
    JobTask jtask = (JobTask) aErrorable;
    Error error = jtask.getError();
    Assert.notNull(error,"error must not be null");
    logger.debug("Erring task {}: {}\n{}", jtask.getId(), error.getMessage());
    MutableJobTask mtask = new MutableJobTask(jtask);
    if(jtask.getRetry() > 0) {
      mtask.setRetryAttempts(jtask.getRetryAttempts()+1);
      jobTaskRepository.update(mtask);
      taskExecutor.execute(mtask);
    }
    else {
      mtask.setStatus(TaskStatus.FAILED);
      Job job = jobRepository.findJobByTaskId (mtask.getId());
      Assert.notNull(job,"job not found for task: " + mtask.getId());
      MutableJob mjob = new MutableJob (job);
      Assert.notNull(mjob,String.format("No job found for task %s ",mtask.getId()));
      mjob.setStatus(JobStatus.FAILED);
      mjob.setFailedDate(new Date ());
      jobTaskRepository.update(mtask);
      jobRepository.update(mjob);
    }
  }
  

  @Override
  protected boolean canHandle(Errorable aErrorable) {
    return (aErrorable instanceof JobTask);
  }

  public void setJobRepository(JobRepository aJobRepository) {
    jobRepository = aJobRepository;
  }

  public void setJobTaskRepository(JobTaskRepository aJobTaskRepository) {
    jobTaskRepository = aJobTaskRepository;
  }
  
  public void setTaskExecutor(TaskExecutor aTaskExecutor) {
    taskExecutor = aTaskExecutor;
  }

}
