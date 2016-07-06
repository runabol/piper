package com.creactiviti.piper.core;

public interface ContextRepository<T extends Context> {

  T findForJobId (String aJobId);
  
  T save (T aContext);
  
}
