/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core;

import java.util.Date;

import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.JobTaskRepository;
import com.creactiviti.piper.core.task.TaskStatus;

/**
 * 
 * @author Arik Cohen
 * @since Apr 24, 2017
 */
public class EachTaskCompletionHandler implements TaskCompletionHandler {

  private final JobTaskRepository jobTaskRepository;
  private final TaskCompletionHandler taskCompletionHandler;
  
  public EachTaskCompletionHandler(JobTaskRepository aJobTaskRepository, TaskCompletionHandler aTaskCompletionHandler) {
    jobTaskRepository = aJobTaskRepository;
    taskCompletionHandler = aTaskCompletionHandler;
  }
  
  @Override
  public void handle (JobTask aJobTask) {
    MutableJobTask mtask = MutableJobTask.createForUpdate(aJobTask);
    mtask.setStatus(TaskStatus.COMPLETED);
    mtask.setEndTime(new Date());
    long updateSubTask = jobTaskRepository.completeSubTask(mtask);
    if(updateSubTask == 0) {
      taskCompletionHandler.handle(jobTaskRepository.findOne(aJobTask.getParentId()));
    }
  }

  @Override
  public boolean canHandle(JobTask aJobTask) {
    return aJobTask.getParentId()!=null;
  }

}
