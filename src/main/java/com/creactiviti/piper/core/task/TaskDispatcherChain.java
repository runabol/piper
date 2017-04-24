/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class TaskDispatcherChain implements TaskDispatcher<Task> {
  
  @Autowired
  private List<TaskDispatcherResolver> resolvers = new ArrayList<>();
  
  @Override
  public void dispatch (Task aTask) {
    for(TaskDispatcherResolver resolver : resolvers) {
      TaskDispatcher executor = resolver.resolve(aTask);
      if(executor != null) {
        executor.dispatch(aTask);
        return;
      }
    }
    throw new IllegalArgumentException("Unable to execute task: " + aTask);
  }

}
