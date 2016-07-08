package com.creactiviti.piper.core.job;

import com.creactiviti.piper.core.JobStatus;
import com.creactiviti.piper.core.JobTask;
import com.creactiviti.piper.core.Task;


public interface JobRepository<T extends Job> {
  
  T getJobById (String aId);
  
  T save (T aJob);
  
  T updateStatus (T aJob, JobStatus aJobStatus);
  
  Task nextTask (T aJob);
  
  T updateTask (T aJob, JobTask aTask);

  T getJobByTaskId (String aTaskId);
  
}
