package com.creactiviti.piper.taskhandler.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.TaskHandler;
import com.creactiviti.piper.core.task.JobTask;

@Component
public class Print implements TaskHandler<Object> {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public Object handle (JobTask aTask) {
    log.info(aTask.getInput().getRequiredString("text"));
    return null;
  }

}
