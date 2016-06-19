package com.creactiviti.piper.core;

import java.util.Map;

public class SimpleTask extends MapObject implements Task {

  private TaskStatus taskStatus = TaskStatus.CREATED;
  
  public SimpleTask(Map<String, Object> aSource) {
    super(aSource);
  }

  @Override
  public String getHandler() {
    return getString("handler");
  }

  @Override
  public String getName() {
    return getString("name");
  }

  @Override
  public String getReturns() {
    return getString("returns");
  }
  
  @Override
  public String getNode() {
    return getString("node");
  }
  
  @Override
  public TaskStatus getTaskStatus() {
    return taskStatus;
  }
  
}