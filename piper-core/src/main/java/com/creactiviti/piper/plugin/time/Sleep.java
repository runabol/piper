/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.plugin.time;

import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

@Component
public class Sleep implements TaskHandler<Object> {

  @Override
  public Object handle (Task aTask) throws InterruptedException {
    Thread.sleep(aTask.getLong("millis", 1000));
    return null;
  }

}
