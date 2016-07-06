package com.creactiviti.piper.core;

import java.util.Map;

public class MutableContext extends MapObject implements Context {

  public MutableContext (String aJobId, Map<String, Object> aSource) {
    super(aSource);
    put("__jobId", aJobId);
  }
  
  @Override
  public String getJobId() {
    return getString("__jobId");
  }
  
}
