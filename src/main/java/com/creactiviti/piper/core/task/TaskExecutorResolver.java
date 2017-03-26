package com.creactiviti.piper.core.task;

public interface TaskExecutorResolver {

  TaskExecutor resolve (JobTask aTask);
  
}
