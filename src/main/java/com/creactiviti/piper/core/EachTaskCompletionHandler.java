/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core;

import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.creactiviti.piper.core.task.CounterRepository;
import com.creactiviti.piper.core.task.TaskExecution;
import com.creactiviti.piper.core.task.TaskExecutionRepository;
import com.creactiviti.piper.core.task.TaskStatus;

/**
 * 
 * @author Arik Cohen
 * @since Apr 24, 2017
 */
public class EachTaskCompletionHandler implements TaskCompletionHandler {

  private final TaskExecutionRepository taskExecutionRepo;
  private final TaskCompletionHandler taskCompletionHandler;
  private final CounterRepository counterRepository;
  
  public EachTaskCompletionHandler(TaskExecutionRepository aTaskExecutionRepo, TaskCompletionHandler aTaskCompletionHandler, CounterRepository aCounterRepository) {
    taskExecutionRepo = aTaskExecutionRepo;
    taskCompletionHandler = aTaskCompletionHandler;
    counterRepository = aCounterRepository;
  }
  
  @Override
  public void handle (TaskExecution aTaskExecution) {
    SimpleTaskExecution mtask = SimpleTaskExecution.createForUpdate(aTaskExecution);
    mtask.setStatus(TaskStatus.COMPLETED);
    taskExecutionRepo.merge(mtask);
    long subtasksLeft = counterRepository.decrement(aTaskExecution.getParentId());
    if(subtasksLeft == 0) {
      taskCompletionHandler.handle(taskExecutionRepo.findOne(aTaskExecution.getParentId()));
    }
  }

  @Override
  public boolean canHandle(TaskExecution aTaskExecution) {
    return aTaskExecution.getParentId()!=null;
  }

}
