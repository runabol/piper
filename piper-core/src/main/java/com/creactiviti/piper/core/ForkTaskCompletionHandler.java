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
 * Handles {@link TaskExecution} completions which are the child 
 * execution tasks of a parent <code>fork</code> {@link TaskExecution}.
 * 
 * This handler will either execute the next task in the branch, or if 
 * arrived at the last task, it will complete the branch and check if
 * all branches are now complete. If so, it will complete the overall 
 * <code>fork</code> task.
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
    if(aTaskExecution.getTaskNumber() < branch.size()) {
      Map<String,Object> task = (Map<String, Object>) branch.get(aTaskExecution.getTaskNumber());
      SimpleTaskExecution execution = SimpleTaskExecution.createFromMap(task);
      execution.setId(UUIDGenerator.generate());
      execution.setStatus(TaskStatus.CREATED);
      execution.setCreateTime(new Date());
      execution.set("branch", aTaskExecution.getInteger("branch"));
      execution.setTaskNumber(aTaskExecution.getTaskNumber()+1);
      execution.setJobId(aTaskExecution.getJobId());
      execution.setParentId(aTaskExecution.getParentId());
      execution.setPriority(aTaskExecution.getPriority());
      Context context = contextRepository.peek(aTaskExecution.getParentId()+"/"+aTaskExecution.getInteger("branch"));
      contextRepository.push(execution.getId(), context);
      TaskExecution evaluatedExecution = taskEvaluator.evaluate(execution, context);
      taskExecutionRepo.create(evaluatedExecution);
      taskDispatcher.dispatch(evaluatedExecution);
    }
    else {
      long branchesLeft = counterRepository.decrement(aTaskExecution.getParentId());
      if(branchesLeft == 0) {
        SimpleTaskExecution forkTask = SimpleTaskExecution.createForUpdate(taskExecutionRepo.findOne(aTaskExecution.getParentId()));
        forkTask.setEndTime(new Date ());
        forkTask.setExecutionTime(forkTask.getEndTime().getTime()-forkTask.getStartTime().getTime());
        taskCompletionHandler.handle(forkTask);
      }
    }
  }

  @Override
  public boolean canHandle (TaskExecution aTaskExecution) {
    return aTaskExecution.getParentId()!=null && aTaskExecution.get("branch")!=null;
  }

}
