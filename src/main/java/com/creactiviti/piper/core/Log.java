package com.creactiviti.piper.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Log implements TaskHandler<Object> {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public Object handle (Task aTask) {
    log.info(aTask.getString("text"));
    return null;
  }

}
