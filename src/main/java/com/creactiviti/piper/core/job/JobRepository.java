package com.creactiviti.piper.core.job;

import com.creactiviti.piper.core.task.JobTask;


public interface JobRepository<T extends Job> {
  
  T findOne (String aId);
  
  T save (T aJob);
  
  T updateStatus (T aJob, JobStatus aJobStatus);
  
  JobTask nextTask (T aJob);
  
  T updateTask (T aJob, JobTask aTask);

  T findJobByTaskId (String aTaskId);
  
}
