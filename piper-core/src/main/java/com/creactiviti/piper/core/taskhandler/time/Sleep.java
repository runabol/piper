
package com.creactiviti.piper.core.taskhandler.time;

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
