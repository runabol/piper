package com.creactiviti.piper.core;

import java.util.List;

import com.creactiviti.piper.core.task.JobTask;

public class TaskCompletionHandlerChain implements TaskCompletionHandler {

  private final List<TaskCompletionHandler> taskCompletionHandlers;
  
  public TaskCompletionHandlerChain(List<TaskCompletionHandler> aTaskCompletionHandlers) {
    taskCompletionHandlers = aTaskCompletionHandlers;
  }
  
  @Override
  public void handle (JobTask aJobTask) {
    for(TaskCompletionHandler handler : taskCompletionHandlers) {
      if(handler.canHandle(aJobTask)) {
        handler.handle(aJobTask);
      }
    }
  }

  @Override
  public boolean canHandle(JobTask aJobTask) {
    return true;
  }

}
