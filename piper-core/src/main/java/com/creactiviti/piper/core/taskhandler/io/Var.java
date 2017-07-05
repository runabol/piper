
package com.creactiviti.piper.core.taskhandler.io;

import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

/**
 * 
 * @author Arik Cohen
 * @since May 11, 2017
 */
@Component
public class Var implements TaskHandler<Object> {

  @Override
  public Object handle (Task aTask) {
    return aTask.getRequired("value");
  }

}
