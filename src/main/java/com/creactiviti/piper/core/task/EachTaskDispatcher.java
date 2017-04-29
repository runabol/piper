/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.context.MapContext;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.messenger.Queues;
import com.creactiviti.piper.core.uuid.UUIDGenerator;

/**
 * A {@link TaskDispatcher} implementation which implements a parallel
 * for-each construct. The dispatcher works by executing 
 * the <code>iteratee</code> function on each item on the <code>list</code>.
 * 
 * @author Arik Cohen
 * @since Apr 25, 2017
 */
public class EachTaskDispatcher implements TaskDispatcher<JobTask>, TaskDispatcherResolver {

  private final TaskDispatcher taskDispatcher;
  private final TaskEvaluator taskEvaluator = new SpelTaskEvaluator();
  private final JobTaskRepository jobTaskRepository;
  private final Messenger messenger;
  private final ContextRepository contextRepository;

  public EachTaskDispatcher (TaskDispatcher aTaskDispatcher, JobTaskRepository aJobTaskRepository, Messenger aMessenger, ContextRepository aContextRepository) {
    taskDispatcher = aTaskDispatcher;
    jobTaskRepository = aJobTaskRepository;
    messenger = aMessenger;
    contextRepository = aContextRepository;
  }

  @Override
  public void dispatch (JobTask aTask) {
    List<Object> list = aTask.getList("list", Object.class);
    Assert.notNull(list,"'list' property can't be null");
    Map<String, Object> iteratee = aTask.getMap("iteratee");
    Assert.notNull(list,"'iteratee' property can't be null");
    if(list.size() > 0) {
      for(Object item : list) {
        MutableJobTask eachTask = MutableJobTask.createFromMap(iteratee);
        eachTask.setId(UUIDGenerator.generate());
        eachTask.setParentId(aTask.getId());
        eachTask.setStatus(TaskStatus.CREATED);
        eachTask.setJobId(aTask.getJobId());
        eachTask.setCreationDate(new Date());
        MapContext context = new MapContext (contextRepository.peek(aTask.getJobId()));
        context.setId(UUIDGenerator.generate());
        context.set(aTask.getString("itemVar","item"), item);
        contextRepository.push(aTask.getJobId(), context);
        JobTask evaluatedEachTask = taskEvaluator.evaluate(eachTask, context);
        jobTaskRepository.create(evaluatedEachTask);
        taskDispatcher.dispatch(evaluatedEachTask);
        contextRepository.pop(aTask.getJobId());
      }
    }
    else {
      MutableJobTask completion = MutableJobTask.createForUpdate(aTask);
      completion.setCompletionDate(new Date());
      messenger.send(Queues.COMPLETIONS, completion);
    }
  }

  @Override
  public TaskDispatcher resolve (Task aTask) {
    if(aTask.getType().equals("each")) {
      return this;
    }
    return null;
  }

}
