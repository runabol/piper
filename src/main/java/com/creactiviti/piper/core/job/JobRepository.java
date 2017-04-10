/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.job;

import com.creactiviti.piper.core.Page;
import com.creactiviti.piper.core.task.JobTask;

public interface JobRepository {
  
  Page<Job> findAll (int aPageNumber);
  
  Job findOne (String aJobId);
  
  void create (Job aJob);
  
  void update (Job aJob);
  
  void create (JobTask aJobTask);
  
  void update (JobTask aJobTask);
  
  Job findJobByTaskId (String aTaskId);
  
  JobTask findTask (String aTaskId);
  
  int countRunningJobs ();
  
  int countCompletedJobsToday ();
  
  int countCompletedJobsYesterday ();
  
}
