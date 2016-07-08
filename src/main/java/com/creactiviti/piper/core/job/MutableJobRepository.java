package com.creactiviti.piper.core.job;

import com.creactiviti.piper.core.task.JobTask;


public interface MutableJobRepository<T extends MutableJob> {
  
  T findOne (String aId);
  
  T save (T aJob);
  
  JobTask nextTask (T aJob);
  
  T findJobByTaskId (String aTaskId);
  
}
