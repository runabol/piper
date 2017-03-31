/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.taskhandler.random;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.TaskHandler;
import com.creactiviti.piper.core.task.JobTask;


/**
 * a {@link TaskHandler} implementaion which generates a 
 * random integer.
 * 
 * @author Arik Cohen
 * @since Mar 30, 2017
 */
@Component
public class RandomInt implements TaskHandler<Object> {

  @Override
  public Object handle(JobTask aTask) throws Exception {
    int startInclusive = aTask.getInteger("startInclusive", 0);
    int endInclusive = aTask.getInteger("endInclusive", 100);
    return RandomUtils.nextInt(startInclusive, endInclusive);
  }

}
