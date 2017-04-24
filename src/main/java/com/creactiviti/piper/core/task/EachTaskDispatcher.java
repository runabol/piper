/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.List;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EachTaskDispatcher implements TaskDispatcher<JobTask>, TaskDispatcherResolver {

  
  @Override
  public void dispatch (JobTask aTask) {
    List<Object> list = aTask.getList("list", Object.class);
    Map<String, Object> iteratee = aTask.getMap("iteratee");
    toString();
  }

  @Override
  public TaskDispatcher resolve (Task aTask) {
    if(aTask.getType().equals("each")) {
      return this;
    }
    return null;
  }
  
}
