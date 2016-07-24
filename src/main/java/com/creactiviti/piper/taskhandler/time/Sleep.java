package com.creactiviti.piper.taskhandler.time;

import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.TaskHandler;
import com.creactiviti.piper.core.task.JobTask;

@Component
public class Sleep implements TaskHandler<Object> {

  @Override
  public Object handle (JobTask aTask) throws InterruptedException {
    Thread.sleep(aTask.getLong("milliseconds", 1000));
    return null;
  }

}
