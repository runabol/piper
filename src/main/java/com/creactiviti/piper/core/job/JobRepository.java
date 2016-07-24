package com.creactiviti.piper.core.job;

import com.creactiviti.piper.core.task.JobTask;



public interface JobRepository {
  
  Job findOne (String aJobId);
  
  Job save (Job aJob);
  
  Job findJobByTaskId (String aTaskId);

  JobTask nextTask (Job aJob);
  
}
