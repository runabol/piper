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

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultTaskExecutor implements TaskExecutor, TaskExecutorResolver {

  private Messenger messenger;
  
  private static final String DEFAULT_PREFIX = "worker";
  private static final String DEFAULT_SUFFIX = "tasks";
  
  @Override
  public void execute (JobTask aTask) {
    String node = aTask.getNode();
    Assert.notNull(messenger,"messenger not configured");
    messenger.send(calculateRoutingKey(node), aTask);
  }
  
  private String calculateRoutingKey (String aNode) {
    StringBuilder sb = new StringBuilder();
    sb.append(DEFAULT_PREFIX)
      .append(".");
    if(aNode!=null) {
      sb.append(aNode)
        .append(".");
    }
    sb.append(DEFAULT_SUFFIX);
    return sb.toString();
  }
  
  @Autowired
  public void setMessenger(Messenger aMessenger) {
    messenger = aMessenger;
  }

  @Override
  public TaskExecutor resolve (JobTask aTask) {
    return this; 
  }

}
