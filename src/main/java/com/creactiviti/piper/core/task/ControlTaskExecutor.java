/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.messenger.Exchanges;
import com.creactiviti.piper.core.messenger.Messenger;

/**
 * 
 * @author Arik Cohen
 * @since Apr 11, 2017
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ControlTaskExecutor implements TaskExecutor, TaskExecutorResolver {

  private Messenger messenger;
  
  @Override
  public void execute(Task aTask) {
    messenger.send(Exchanges.CONTROL+"/"+Exchanges.CONTROL, aTask);
  }

  @Override
  public TaskExecutor resolve(Task aTask) {
    if(aTask instanceof ControlTask) {
      return this; 
    }
    return null;
  }

  @Autowired
  public void setMessenger(Messenger aMessenger) {
    messenger = aMessenger;
  }
  
}
