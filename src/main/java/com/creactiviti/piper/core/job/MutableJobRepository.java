package com.creactiviti.piper.core.job;



public interface MutableJobRepository<T extends MutableJob> {
  
  T findOne (String aId);
  
  T save (T aJob);
  
  T findJobByTaskId (String aTaskId);
  
}
