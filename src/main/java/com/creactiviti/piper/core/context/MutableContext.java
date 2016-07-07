package com.creactiviti.piper.core.context;

import java.util.Map;

import com.creactiviti.piper.core.MapObject;

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
