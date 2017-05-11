/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, May 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.context.MapContext;
import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.messenger.Queues;
import com.creactiviti.piper.core.uuid.UUIDGenerator;

/**
 * 
 * @author Arik Cohen
 * @since May 11, 2017
 */
public class ForkTaskDispatcher implements TaskDispatcher<TaskExecution>, TaskDispatcherResolver {

  private final TaskDispatcher taskDispatcher;
  private final TaskEvaluator taskEvaluator = new SpelTaskEvaluator();
  private final TaskExecutionRepository taskExecutionRepo;
  private final Messenger messenger;
  private final ContextRepository contextRepository;
  private final CounterRepository counterRepository;

  public ForkTaskDispatcher (TaskDispatcher aTaskDispatcher, TaskExecutionRepository aTaskExecutionRepo, Messenger aMessenger, ContextRepository aContextRepository, CounterRepository aCounterRepository) {
    taskDispatcher = aTaskDispatcher;
    taskExecutionRepo = aTaskExecutionRepo;
    messenger = aMessenger;
    contextRepository = aContextRepository;
    counterRepository = aCounterRepository;
  }

  @Override
  public void dispatch (TaskExecution aTask) {
    List<List> branches = aTask.getList("branches", List.class);
    Assert.notNull(branches,"'branches' property can't be null");
    if(branches.size() > 0) {
      counterRepository.set(aTask.getId(), branches.size());
      for(int i=0; i<branches.size(); i++) {
        List branch = branches.get(i);
        Assert.isTrue(branch.size()>0, "branch " + i + " does not contain any tasks");
        Map<String,Object> task = (Map<String, Object>) branch.get(0);
        SimpleTaskExecution execution = SimpleTaskExecution.createFromMap(task);
        execution.setId(UUIDGenerator.generate());
        execution.setStatus(TaskStatus.CREATED);
        execution.setCreateTime(new Date());
        execution.set("branch", i);
        execution.setTaskNumber(0);
        execution.setJobId(aTask.getJobId());
        execution.setParentId(aTask.getId());
        MapContext context = new MapContext (contextRepository.peek(aTask.getId()));
        contextRepository.push(aTask.getId()+"/"+i, context);
        TaskExecution evaluatedExecution = taskEvaluator.evaluate(execution, context);
        taskExecutionRepo.create(evaluatedExecution);
        taskDispatcher.dispatch(evaluatedExecution);
      }
    }
    else {
      SimpleTaskExecution completion = SimpleTaskExecution.createForUpdate(aTask);
      completion.setEndTime(new Date());
      messenger.send(Queues.COMPLETIONS, completion);
    }
  }

  @Override
  public TaskDispatcher resolve (Task aTask) {
    if(aTask.getType().equals("fork")) {
      return this;
    }
    return null;
  }

}
