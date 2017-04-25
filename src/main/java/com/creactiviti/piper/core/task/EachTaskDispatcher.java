/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.creactiviti.piper.core.context.MapContext;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.uuid.UUIDGenerator;

public class EachTaskDispatcher implements TaskDispatcher<JobTask>, TaskDispatcherResolver {
  
  private final TaskDispatcher taskDispatcher;
  private final TaskEvaluator taskEvaluator = new SpelTaskEvaluator();
  private final JobTaskRepository jobTaskRepository;
  
  public EachTaskDispatcher(TaskDispatcher aTaskDispatcher, JobTaskRepository aJobTaskRepository) {
    taskDispatcher = aTaskDispatcher;
    jobTaskRepository = aJobTaskRepository;
  }
  
  @Override
  public void dispatch (JobTask aTask) {
    List<Object> list = aTask.getList("list", Object.class);
    Map<String, Object> iteratee = aTask.getMap("iteratee");
    for(Object item : list) {
      MutableJobTask eachTask = MutableJobTask.createFromMap(iteratee);
      eachTask.setId(UUIDGenerator.generate());
      eachTask.setParentId(aTask.getId());
      eachTask.setStatus(TaskStatus.CREATED);
      eachTask.setJobId(aTask.getJobId());
      eachTask.setCreationDate(new Date());
      MapContext context = new MapContext(Collections.singletonMap(aTask.getString("itemVar","item"), item));
      JobTask evaluatedEachTask = taskEvaluator.evaluate(eachTask, context);
      jobTaskRepository.create(evaluatedEachTask);
      taskDispatcher.dispatch(evaluatedEachTask);
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
