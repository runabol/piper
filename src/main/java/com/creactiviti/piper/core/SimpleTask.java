package com.creactiviti.piper.core;

import java.util.Map;

public class SimpleTask extends MapObject implements Task {

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
  
}