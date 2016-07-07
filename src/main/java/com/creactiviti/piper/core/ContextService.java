package com.creactiviti.piper.core;

public interface ContextService<T extends Context> {

  T findForJobId (String aJobId);
  
  T save (T aContext);
  
}
