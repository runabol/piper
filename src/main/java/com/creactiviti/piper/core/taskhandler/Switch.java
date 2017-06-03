/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, May 2017
 */
package com.creactiviti.piper.core.taskhandler;

import java.util.List;

import org.springframework.util.Assert;

import com.creactiviti.piper.core.MapObject;
import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

/**
 * Implements a switch statement.
 * 
 * @author Arik Cohen
 * @since May 23, 2017
 */
//@Component
public class Switch implements TaskHandler<Object> {

  @Override
  public Object handle (Task aTask) throws Exception {
    Object expression = aTask.getRequired("expression");
    List<MapObject> cases = aTask.getList("cases", MapObject.class);
    Assert.notNull(cases,"you must specify 'caes' in a switch statement");
    Object defaultCase = aTask.get("default");
    for(MapObject oneCase : cases) {
      Object key = oneCase.getRequired("key");
      Object value = oneCase.getRequired("value");
      if(key.equals(expression)) {
        return value;
      }
    }
    return defaultCase;
  }

}