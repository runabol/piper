package com.creactiviti.piper.core;

import java.util.Map;

import com.creactiviti.piper.core.uuid.UUIDFactory;


public class MutableTask extends MapObject implements Task, Mutator {

  public MutableTask (Task aSource) {
    super(aSource.toMap());
    set("id", aSource.getId());
  }
  
  public MutableTask (Map<String, Object> aSource) {
    super(aSource);
    setIfNull("id", UUIDFactory.create());
    setIfNull("status", TaskStatus.CREATED);
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
  public String getStatus() {
    return getString("status");
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
  
  public void setStatus (String aStatus) {
    set("status",aStatus);
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