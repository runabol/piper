package com.creactiviti.piper.core;

import java.util.Map;

import com.creactiviti.piper.core.uuid.UUIDFactory;


public class MutableTask extends MapObject implements Task, Mutator {

  private TaskStatus status;
  
  public MutableTask (Task aSource) {
    super(aSource.toMap());
    set("id", aSource.getId());
    status=aSource.getStatus();
  }
  
  public MutableTask (Map<String, Object> aSource) {
    super(aSource);
    set("id", UUIDFactory.create());
    status=TaskStatus.CREATED;
  }
  
  @Override
  public String getId() {
    return getString("id");
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
  public TaskStatus getStatus() {
    return status;
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
  
  public void setStatus (TaskStatus aStatus) {
    status=aStatus;
  }

  @Override
  public void set (String aKey, Object aValue) {
    put(aKey, aValue);
  }
  
  @Override
  public void setIfNull(String aKey, Object aValue) {
    if(get(aKey)==null) {
      set(aKey, aValue);
    }
  }
  
}