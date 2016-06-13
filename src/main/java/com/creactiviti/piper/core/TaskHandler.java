package com.creactiviti.piper.core;

public interface TaskHandler<T> {

  T handle (Task aTask);
  
}
