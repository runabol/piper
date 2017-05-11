/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, May 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;

import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.messenger.Queues;

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
    List<Object> branches = aTask.getList("branches", Object.class);
    Assert.notNull(branches,"'branches' property can't be null");
    if(branches.size() > 0) {
      for(Object item : branches) {
        toString();
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
