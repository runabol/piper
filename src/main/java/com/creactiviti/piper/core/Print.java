package com.creactiviti.piper.core;

import org.springframework.stereotype.Component;

@Component
public class Print implements TaskHandler<Object> {

  @Override
  public Object handle (Task aTask) {
    return null;
  }

}
