package com.creactiviti.piper.core.job;



public interface JobRepository {
  
  MutableJob findOne (String aId);
  
  MutableJob save (MutableJob aJob);
  
  MutableJob findJobByTaskId (String aTaskId);
  
}
