package com.creactiviti.piper.core;

public interface Worker {

  void start (Task aTask);
  
  void stop (String aTaskId);
  
}
