package com.creactiviti.piper.core.job;

public interface JobRepository {
  
  Job findOne (String aJobId);
  
  Job save (Job aJob);
  
  Job findJobByTaskId (String aTaskId);
  
}
