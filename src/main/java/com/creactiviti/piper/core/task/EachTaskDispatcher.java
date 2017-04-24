/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.context.MapContext;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.uuid.UUIDGenerator;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EachTaskDispatcher implements TaskDispatcher<JobTask>, TaskDispatcherResolver {
  
  private TaskDispatcher taskDispatcher;
  private TaskEvaluator taskEvaluator = new SpelTaskEvaluator();
  
  // type: each
  // list: [1, 2, 3]
  // itemVar: item1
  // iteratee: 
  //   type: each
  //   list: [4,5,6]
  //   itemVar: item2
  //   iteratee: 
  //     type: print
  //     text: "${item1} {item2}"
  
  
  @Override
  public void dispatch (JobTask aTask) {
    List<Object> list = aTask.getList("list", Object.class);
    Map<String, Object> iteratee = aTask.getMap("iteratee");
    for(Object item : list) {
      MutableJobTask eachTask = MutableJobTask.createFromMap(iteratee);
      eachTask.setId(UUIDGenerator.generate());
      eachTask.setParentId(aTask.getId());
      MapContext context = new MapContext(Collections.singletonMap(aTask.getString("itemVar","item"), item));
      JobTask evaluatedEachTask = taskEvaluator.evaluate(eachTask, context);
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

  @Autowired
  public void setTaskDispatcher(TaskDispatcher aTaskDispatcher) {
    taskDispatcher = aTaskDispatcher;
  }
  
  public void setTaskEvaluator(TaskEvaluator aTaskEvaluator) {
    taskEvaluator = aTaskEvaluator;
  }
  
}
