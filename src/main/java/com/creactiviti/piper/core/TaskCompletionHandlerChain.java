package com.creactiviti.piper.core;

import java.util.ArrayList;
import java.util.List;

import com.creactiviti.piper.core.task.JobTask;

public class TaskCompletionHandlerChain implements TaskCompletionHandler {

  private List<TaskCompletionHandler> taskCompletionHandlers = new ArrayList<>();
  
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
  
  public void setTaskCompletionHandlers(List<TaskCompletionHandler> aTaskCompletionHandlers) {
    taskCompletionHandlers = aTaskCompletionHandlers;
  }

}
