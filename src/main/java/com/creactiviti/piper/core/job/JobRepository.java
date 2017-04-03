/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.job;

import java.util.List;

import com.creactiviti.piper.core.task.JobTask;

public interface JobRepository {
  
  List<Job> findAll ();
  
  Job findOne (String aJobId);
  
  Job save (Job aJob);
  
  JobTask create (JobTask aJobTask);
  
  JobTask update (JobTask aJobTask);
  
  Job findJobByTaskId (String aTaskId);
  
}
