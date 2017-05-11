/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.creactiviti.piper.core.context.Context;
import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.context.MapContext;
import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.creactiviti.piper.core.task.CounterRepository;
import com.creactiviti.piper.core.task.SpelTaskEvaluator;
import com.creactiviti.piper.core.task.TaskDispatcher;
import com.creactiviti.piper.core.task.TaskEvaluator;
import com.creactiviti.piper.core.task.TaskExecution;
import com.creactiviti.piper.core.task.TaskExecutionRepository;
import com.creactiviti.piper.core.task.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDGenerator;

/**
 * 
 * @author Arik Cohen
 * @since May 11, 2017
 */
public class ForkTaskCompletionHandler implements TaskCompletionHandler {

  private final TaskExecutionRepository taskExecutionRepo;
  private final TaskCompletionHandler taskCompletionHandler;
  private final CounterRepository counterRepository;
  private final TaskDispatcher taskDispatcher;
  private final ContextRepository contextRepository;
  private final TaskEvaluator taskEvaluator = new SpelTaskEvaluator();
  
  public ForkTaskCompletionHandler(TaskExecutionRepository aTaskExecutionRepo, TaskCompletionHandler aTaskCompletionHandler, CounterRepository aCounterRepository, TaskDispatcher aTaskDispatcher, ContextRepository aContextRepository) {
    taskExecutionRepo = aTaskExecutionRepo;
    taskCompletionHandler = aTaskCompletionHandler;
    counterRepository = aCounterRepository;
    taskDispatcher = aTaskDispatcher;
    contextRepository = aContextRepository;
  }
  
  @Override
  public void handle (TaskExecution aTaskExecution) {
    SimpleTaskExecution mtask = SimpleTaskExecution.createForUpdate(aTaskExecution);
    mtask.setStatus(TaskStatus.COMPLETED);
    taskExecutionRepo.merge(mtask);
    
    if(aTaskExecution.getOutput() != null && aTaskExecution.getName() != null) {
      Context context = contextRepository.peek(aTaskExecution.getParentId()+"/"+aTaskExecution.getInteger("branch"));
      MapContext newContext = new MapContext(context.asMap());
      newContext.put(aTaskExecution.getName(), aTaskExecution.getOutput());
      contextRepository.push(aTaskExecution.getParentId()+"/"+aTaskExecution.getInteger("branch"), newContext);
    }
    
    TaskExecution fork = taskExecutionRepo.findOne(aTaskExecution.getParentId());
    List<List> list = fork.getList("branches", List.class);
    List<Map<String,Object>> branch = list.get(aTaskExecution.getInteger("branch"));
    if(aTaskExecution.getTaskNumber()+1 < branch.size()) {
      Map<String,Object> task = (Map<String, Object>) branch.get(aTaskExecution.getTaskNumber()+1);
      SimpleTaskExecution execution = SimpleTaskExecution.createFromMap(task);
      execution.setId(UUIDGenerator.generate());
      execution.setStatus(TaskStatus.CREATED);
      execution.setCreateTime(new Date());
      execution.set("branch", aTaskExecution.getInteger("branch"));
      execution.setTaskNumber(aTaskExecution.getTaskNumber()+1);
      execution.setJobId(aTaskExecution.getJobId());
      execution.setParentId(aTaskExecution.getParentId());
      Context context = contextRepository.peek(aTaskExecution.getParentId()+"/"+aTaskExecution.getInteger("branch"));
      TaskExecution evaluatedExecution = taskEvaluator.evaluate(execution, context);
      taskExecutionRepo.create(evaluatedExecution);
      taskDispatcher.dispatch(evaluatedExecution);
    }
    else {
      long branchesLeft = counterRepository.decrement(aTaskExecution.getParentId());
      if(branchesLeft == 0) {
        taskCompletionHandler.handle(taskExecutionRepo.findOne(aTaskExecution.getParentId()));
      }
    }
  }

  @Override
  public boolean canHandle (TaskExecution aTaskExecution) {
    String parentId = aTaskExecution.getParentId();
    if(parentId!=null) {
      TaskExecution parentExecution = taskExecutionRepo.findOne(parentId);
      return parentExecution.getType().equals("fork");
    }
    return false;
  }

}
