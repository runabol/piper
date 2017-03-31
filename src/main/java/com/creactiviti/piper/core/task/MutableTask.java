package com.creactiviti.piper.core.task;

import java.util.Map;

import com.creactiviti.piper.core.MapObject;
import com.creactiviti.piper.core.Task;


public class MutableTask extends MapObject implements Task {

  public MutableTask (Task aSource) {
    super(aSource.toMap());
  }
  
  public MutableTask (Map<String, Object> aSource) {
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
  public String getLabel() {
    return getString("label");
  }

  @Override
  public String getNode() {
    return getString("node");
  }
  
}