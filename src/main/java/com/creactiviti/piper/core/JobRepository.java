package com.creactiviti.piper.core;

import java.util.List;

public interface JobRepository<T extends Job> {
  
  List<T> findAll ();
  
  T findOne (String aId);
  
  T save (T aJob);
  
  T updateStatus (T aJob, JobStatus aJobStatus);
  
}
