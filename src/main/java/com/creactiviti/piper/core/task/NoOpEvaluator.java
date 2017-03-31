package com.creactiviti.piper.core.task;

public class NoOpEvaluator implements TaskEvaluator {

  @Override
  public JobTask evaluate(JobTask aJobTask) {
    return aJobTask;
  }

}
