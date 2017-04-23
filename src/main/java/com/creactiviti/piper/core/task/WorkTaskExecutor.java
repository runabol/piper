/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.messenger.Queues;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class WorkTaskExecutor implements TaskExecutor<JobTask>, TaskExecutorResolver {

  private Messenger messenger;
  
  private static final String DEFAULT_QUEUE = Queues.TASKS;
  
  @Override
  public void execute (JobTask aTask) {
    Assert.notNull(messenger,"messenger not configured");
    messenger.send(calculateRoutingKey(aTask), aTask);
  }
  
  private String calculateRoutingKey (Task aTask) {
    JobTask jtask = (JobTask) aTask;
    return jtask.getNode()!=null?jtask.getNode():DEFAULT_QUEUE;
  }
  
  @Autowired
  public void setMessenger(Messenger aMessenger) {
    messenger = aMessenger;
  }

  @Override
  public TaskExecutor resolve (Task aTask) {
    if(aTask instanceof JobTask) {
      return this; 
    }
    return null;
  }

}
