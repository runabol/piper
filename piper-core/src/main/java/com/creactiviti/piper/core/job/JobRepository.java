
package com.creactiviti.piper.core.job;

import com.creactiviti.piper.core.Page;

public interface JobRepository {
  
  Page<Job> findAll (int aPageNumber);
  
  Job findOne (String aId);
  
  void create (Job aJob);
  
  Job merge (Job aJob);
  
  Job findJobByTaskId (String aTaskId);
  
  int countRunningJobs ();
  
  int countCompletedJobsToday ();
  
  int countCompletedJobsYesterday ();
  
}
