package com.creactiviti.piper.core;

import java.util.Map;

import org.jgroups.util.UUID;

public class MutableTask extends MapObject implements Task, Mutator {

  private TaskStatus taskStatus = TaskStatus.CREATED;
  
  public MutableTask (Map<String, Object> aSource) {
    super(aSource);
    set("__id", UUID.randomUUID().toString());
  }
  
  @Override
  public String getId() {
    return getString("__id");
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
  public String getJobId() {
    return getString("__jobId");
  }
  
  public void setJobId(String aJobId) {
    set("__jobId", aJobId);
  }
  
  @Override
  public TaskStatus getTaskStatus() {
    return taskStatus;
  }
  
  @Override
  public Object getOutput() {
    return get("__output");
  }
  
  @Override
  public <T> T getOutput(Class<T> aType) {
    return get("__output", aType);
  }

  public void setOutput (Object aOutput) {
    set("__output", aOutput);
  }

  @Override
  public void set (String aKey, Object aValue) {
    put(aKey, aValue);
  }
  
}