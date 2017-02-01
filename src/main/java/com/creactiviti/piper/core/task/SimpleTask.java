package com.creactiviti.piper.core.task;

import java.util.Map;

import com.creactiviti.piper.core.MapObject;
import com.creactiviti.piper.core.Task;


public class SimpleTask extends MapObject implements Task {

  public SimpleTask (Task aSource) {
    super(aSource.toMap());
  }
  
  public SimpleTask (Map<String, Object> aSource) {
    super(aSource);
  }
  
  @Override
  public String getType() {
    return getString("type");
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
  
}