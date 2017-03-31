/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.TaskHandler;
import com.creactiviti.piper.core.task.TaskHandlerResolver;

@Component
public class DefaultTaskHandlerResolver implements TaskHandlerResolver {

  private Map<String, TaskHandler<?>> taskHandlers = new HashMap<String, TaskHandler<?>>();
  
  @Override
  public TaskHandler<?> resolve(JobTask aJobTask) {
    TaskHandler<?> taskHandler = taskHandlers.get(aJobTask.getType());
    Assert.notNull(taskHandler,"Unknown task handler: " + aJobTask.getType());
    return taskHandler;
  }

  @Autowired(required=false)
  public void setTaskHandlers(Map<String, TaskHandler<?>> aTaskHandlers) {
    taskHandlers = aTaskHandlers;
  }
  
}
