package com.creactiviti.piper.config;

public class CoordinatorSubscriptions {

  private int completions = 1;
  private int errors = 1;

  public int getCompletions() {
    return completions;
  }
  
  public void setCompletions(int aCompletions) {
    completions = aCompletions;
  }

  public int getErrors() {
    return errors;
  }
  
  public void setErrors(int aErrors) {
    errors = aErrors;
  }
  
}
