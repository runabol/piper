package com.creactiviti.piper.core.task;

import com.creactiviti.piper.core.context.Context;

public class NoOpTaskEvaluator implements TaskEvaluator {

  @Override
  public TaskExecution evaluate(TaskExecution aJobTask, Context aContext) {
    return aJobTask;
  }

}
