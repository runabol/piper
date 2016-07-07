package com.creactiviti.piper.core.context;

public interface ContextService<T extends Context> {

  T getForJobId (String aJobId);
  
  T save (T aContext);
  
}
