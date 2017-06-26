/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.plugin.random;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

/**
 * a {@link TaskHandler} implementaion which can
 * throw an exception based on a probabilty
 * value.
 * 
 * @author Arik Cohen
 * @since Mar 30, 2017
 */
@Component
public class Rogue implements TaskHandler<Object> {

  @Override
  public Object handle(Task aTask) throws Exception {
    float nextFloat = RandomUtils.nextFloat(0, 1);
    float probabilty = aTask.getFloat("probabilty",0.5f);
    Assert.isTrue(probabilty>=0 && probabilty<=1,"probability must be a value between 0 and 1");
    if(nextFloat <= probabilty) {
      throw new IllegalStateException("I'm a rogue exception");
    }
    return null;
  }

}
