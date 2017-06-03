/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Jun 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;

import com.creactiviti.piper.core.MapObject;
import com.creactiviti.piper.core.SwitchTaskCompletionHandler;
import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.context.MapContext;
import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.messenger.Queues;
import com.creactiviti.piper.core.uuid.UUIDGenerator;

/**
 * @author Arik Cohen
 * @since Jun 3, 2017
 * @see SwitchTaskCompletionHandler
 */
public class SwitchTaskDispatcher implements TaskDispatcher<TaskExecution>, TaskDispatcherResolver {

  private TaskDispatcher taskDispatcher;
  private TaskEvaluator taskEvaluator = new SpelTaskEvaluator();
  private TaskExecutionRepository taskExecutionRepo;
  private ContextRepository contextRepository;
  private Messenger messenger;

  @Override
  public void dispatch (TaskExecution aTask) {
    SimpleTaskExecution switchTask = SimpleTaskExecution.createForUpdate(aTask);
    switchTask.setStartTime(new Date ());
    switchTask.setStatus(TaskStatus.STARTED);
    taskExecutionRepo.merge(switchTask);
    List<MapObject> tasks = resolveCase(aTask);
    if(tasks.size() > 0) {
      MapObject task = tasks.get(0);
      SimpleTaskExecution execution = SimpleTaskExecution.createFromMap(task);
      execution.setId(UUIDGenerator.generate());
      execution.setStatus(TaskStatus.CREATED);
      execution.setCreateTime(new Date());
      execution.setTaskNumber(1);
      execution.setJobId(switchTask.getJobId());
      execution.setParentId(switchTask.getId());
      execution.setPriority(switchTask.getPriority());
      MapContext context = new MapContext (contextRepository.peek(switchTask.getId()));
      contextRepository.push(execution.getId(), context);
      TaskExecution evaluatedExecution = taskEvaluator.evaluate(execution, context);
      taskExecutionRepo.create(evaluatedExecution);
      taskDispatcher.dispatch(evaluatedExecution);
    }
    else  {
      SimpleTaskExecution completion = SimpleTaskExecution.createForUpdate(aTask);
      completion.setStartTime(new Date());
      completion.setEndTime(new Date());
      completion.setExecutionTime(0);
      messenger.send(Queues.COMPLETIONS, completion);
    }
  }

  private List<MapObject> resolveCase (TaskExecution aSwitch) {
    Object expression = aSwitch.getRequired("expression");
    List<MapObject> cases = aSwitch.getList("cases", MapObject.class);
    Assert.notNull(cases,"you must specify 'cases' in a switch statement");
    for(MapObject oneCase : cases) {
      Object key = oneCase.getRequired("key");
      List<MapObject> tasks = oneCase.getList("tasks",MapObject.class);
      if(key.equals(expression)) {
        return tasks;
      }
    }
    return aSwitch.getList("default", MapObject.class,Collections.emptyList());
  }

  @Override
  public TaskDispatcher resolve (Task aTask) {
    if(aTask.getType().equals("switch")) {
      return this;
    }
    return null;
  }

  public void setContextRepository(ContextRepository aContextRepository) {
    contextRepository = aContextRepository;
  }

  public void setTaskDispatcher(TaskDispatcher aTaskDispatcher) {
    taskDispatcher = aTaskDispatcher;
  }

  public void setTaskEvaluator(TaskEvaluator aTaskEvaluator) {
    taskEvaluator = aTaskEvaluator;
  }

  public void setTaskExecutionRepo(TaskExecutionRepository aTaskExecutionRepo) {
    taskExecutionRepo = aTaskExecutionRepo;
  }

  public void setMessenger(Messenger aMessenger) {
    messenger = aMessenger;
  }

}
