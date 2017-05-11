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
 * Implements a Fork/Join construct.
 * 
 * <pre>
 *   - type: fork
 *     branches: 
 *       - - name: randomNumber
 *           label: Generate a random number
 *           type: randomInt
 *           startInclusive: 0
 *           endInclusive: 5000
 *         
 *         - type: sleep
 *           millis: ${randomNumber}
 *         
 *       - - name: randomNumber
 *           label: Generate a random number
 *           type: randomInt
 *           startInclusive: 0
 *           endInclusive: 5000
 *         
 *         - type: sleep
 *           millis: ${randomNumber}
 * </pre>
 * 
 * @author Arik Cohen
 * @since May 11, 2017
 */
public class ForkJoinTaskDispatcher implements TaskDispatcher<TaskExecution>, TaskDispatcherResolver {

  private TaskDispatcher taskDispatcher;
  private TaskEvaluator taskEvaluator = new SpelTaskEvaluator();
  private TaskExecutionRepository taskExecutionRepo;
  private Messenger messenger;
  private ContextRepository contextRepository;
  private CounterRepository counterRepository;

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
        contextRepository.push(execution.getId(), context);
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
  
  public void setTaskEvaluator(TaskEvaluator aTaskEvaluator) {
    taskEvaluator = aTaskEvaluator;
  }

  public void setTaskExecutionRepo(TaskExecutionRepository aTaskExecutionRepo) {
    taskExecutionRepo = aTaskExecutionRepo;
  }

}
