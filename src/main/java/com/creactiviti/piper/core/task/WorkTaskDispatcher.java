/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.messenger.Queues;

@Component
public class WorkTaskDispatcher implements TaskDispatcher<TaskExecution>, TaskDispatcherResolver {

  private final Messenger messenger;
  
  private static final String DEFAULT_QUEUE = Queues.TASKS;
  
  public WorkTaskDispatcher (Messenger aMessenger) {
    messenger = aMessenger;
  }
  
  @Override
  public void dispatch (TaskExecution aTask) {
    Assert.notNull(messenger,"messenger not configured");
    messenger.send(calculateRoutingKey(aTask), aTask);
  }
  
  private String calculateRoutingKey (Task aTask) {
    TaskExecution jtask = (TaskExecution) aTask;
    return jtask.getNode()!=null?jtask.getNode():DEFAULT_QUEUE;
  }
  
  @Override
  public TaskDispatcher resolve (Task aTask) {
    if(aTask instanceof TaskExecution) {
      return this; 
    }
    return null;
  }

}
