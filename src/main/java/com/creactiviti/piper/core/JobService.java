package com.creactiviti.piper.core;

import java.util.List;

public interface JobService<T extends Job> {
  
  List<T> findAll ();
  
  T findOne (String aId);
  
  T save (T aJob);
  
  T updateStatus (T aJob, JobStatus aJobStatus);
  
  Task nextTask (T aJob);
  
  T updateTask (T aJob, Task aTask);

  T findByTaskId (String aTaskId);
}
