
package com.creactiviti.piper.core.task;

import java.util.List;

public class TaskDispatcherChain implements TaskDispatcher<Task> {
  
  private List<TaskDispatcherResolver> resolvers;
  
  public TaskDispatcherChain() {
  }
  
  @Override
  public void dispatch (Task aTask) {
    for(TaskDispatcherResolver resolver : resolvers) {
      TaskDispatcher executor = resolver.resolve(aTask);
      if(executor != null) {
        executor.dispatch(aTask);
        return;
      }
    }
    throw new IllegalArgumentException("Unable to execute task: " + aTask);
  }

  public void setResolvers(List<TaskDispatcherResolver> aResolvers) {
    resolvers = aResolvers;
  }

}
