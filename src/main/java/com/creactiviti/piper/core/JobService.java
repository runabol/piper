package com.creactiviti.piper.core;


public interface JobService<T extends Job> {
  
  T getJobById (String aId);
  
  T save (T aJob);
  
  T updateStatus (T aJob, JobStatus aJobStatus);
  
  Task nextTask (T aJob);
  
  T updateTask (T aJob, Task aTask);

  T getJobByTaskId (String aTaskId);
  
}
