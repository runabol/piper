package com.creactiviti.piper.core.context;

import java.util.Map;

import com.creactiviti.piper.core.MapObject;

public class SimpleContext extends MapObject implements Context {

  public SimpleContext (String aJobId, Map<String, Object> aSource) {
    super(aSource);
    put("__jobId", aJobId);
  }
  
  @Override
  public String getJobId() {
    return getString("__jobId");
  }
  
}
