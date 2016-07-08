package com.creactiviti.piper.core;

import com.creactiviti.piper.core.task.JobTask;

public interface TaskHandler<T> {

  T handle (JobTask aTask);
  
}
