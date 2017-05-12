/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, May 2017
 */
package com.creactiviti.piper.core.task;

import com.creactiviti.piper.core.TaskCompletionHandler;
import com.creactiviti.piper.core.job.SimpleTaskExecution;

/**
 * 
 * @author Arik Cohen
 * @since May 12, 2017
 */
public class ParallelTaskCompletionHandler implements TaskCompletionHandler {

  private final TaskExecutionRepository taskExecutionRepo;
  private final TaskCompletionHandler taskCompletionHandler;
  private final CounterRepository counterRepository;
  
  public ParallelTaskCompletionHandler(TaskExecutionRepository aTaskExecutionRepo, TaskCompletionHandler aTaskCompletionHandler, CounterRepository aCounterRepository) {
    taskExecutionRepo = aTaskExecutionRepo;
    taskCompletionHandler = aTaskCompletionHandler;
    counterRepository = aCounterRepository;
  }
  
  @Override
  public void handle (TaskExecution aTaskExecution) {
    SimpleTaskExecution mtask = SimpleTaskExecution.createForUpdate(aTaskExecution);
    mtask.setStatus(TaskStatus.COMPLETED);
    taskExecutionRepo.merge(mtask);
    long tasksLeft = counterRepository.decrement(aTaskExecution.getParentId());
    if(tasksLeft == 0) {
      taskCompletionHandler.handle(taskExecutionRepo.findOne(aTaskExecution.getParentId()));
      counterRepository.delete(aTaskExecution.getParentId());
    }
  }

  @Override
  public boolean canHandle (TaskExecution aTaskExecution) {
    String parentId = aTaskExecution.getParentId();
    if(parentId!=null) {
      TaskExecution parentExecution = taskExecutionRepo.findOne(parentId);
      return parentExecution.getType().equals("parallel");
    }
    return false;
  }

}
