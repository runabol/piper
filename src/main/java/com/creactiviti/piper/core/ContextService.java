package com.creactiviti.piper.core;

public interface ContextService<T extends Context> {

  T getForJobId (String aJobId);
  
  T save (T aContext);
  
}
