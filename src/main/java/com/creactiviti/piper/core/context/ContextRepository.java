package com.creactiviti.piper.core.context;

public interface ContextRepository<T extends Context> {

  T getForJobId (String aJobId);
  
  T save (T aContext);
  
}
