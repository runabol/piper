package com.creactiviti.piper.core.task;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EachTaskExecutor implements TaskExecutor, TaskExecutorResolver {

  @Override
  public void execute(JobTask aTask) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TaskExecutor resolve (JobTask aTask) {
    if(aTask.contains("each")) {
      return this;
    }
    return null;
  }

}
