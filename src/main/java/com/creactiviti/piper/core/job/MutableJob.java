package com.creactiviti.piper.core.job;

import com.creactiviti.piper.core.task.JobTask;

public interface MutableJob extends Job {

  void setStatus (JobStatus aStatus);
  
  void updateTask (JobTask aTask);
  
}
