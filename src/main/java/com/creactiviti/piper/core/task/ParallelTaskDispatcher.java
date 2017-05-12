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
 * @since May 12, 2017
 */
public class ParallelTaskDispatcher implements TaskDispatcher<TaskExecution>, TaskDispatcherResolver {

  private TaskDispatcher taskDispatcher;
  private TaskExecutionRepository taskExecutionRepo;
  private Messenger messenger;
  private ContextRepository contextRepository;
  private CounterRepository counterRepository;

  @Override
  public void dispatch (TaskExecution aTask) {
    List<Map> tasks = aTask.getList("tasks", Map.class);
    Assert.notNull(tasks,"'tasks' property can't be null");
    if(tasks.size() > 0) {
      counterRepository.set(aTask.getId(), tasks.size());
      for(Map task : tasks) {
        SimpleTaskExecution parallelTask = SimpleTaskExecution.createFromMap(task);
        parallelTask.setId(UUIDGenerator.generate());
        parallelTask.setParentId(aTask.getId());
        parallelTask.setStatus(TaskStatus.CREATED);
        parallelTask.setJobId(aTask.getJobId());
        parallelTask.setCreateTime(new Date());
        MapContext context = new MapContext (contextRepository.peek(aTask.getId()));
        contextRepository.push(parallelTask.getId(), context);
        taskExecutionRepo.create(parallelTask);
        taskDispatcher.dispatch(parallelTask);
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
    if(aTask.getType().equals("parallel")) {
      return this;
    }
    return null;
  }

  public void setContextRepository(ContextRepository aContextRepository) {
    contextRepository = aContextRepository;
  }
  
  public void setCounterRepository(CounterRepository aCounterRepository) {
    counterRepository = aCounterRepository;
  }
  
  public void setMessenger(Messenger aMessenger) {
    messenger = aMessenger;
  }
  
  public void setTaskDispatcher(TaskDispatcher aTaskDispatcher) {
    taskDispatcher = aTaskDispatcher;
  }
  
  public void setTaskExecutionRepository(TaskExecutionRepository aTaskExecutionRepo) {
    taskExecutionRepo = aTaskExecutionRepo;
  }
  
}
