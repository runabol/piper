/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Date;

import com.creactiviti.piper.error.Error;

public interface JobTask extends Task {

  String getId ();
  
  String getJobId ();
  
  TaskStatus getStatus ();
  
  Object getOutput ();
  
  Error getError ();
  
  Date getCreationDate ();
  
  Date getCompletionDate ();
  
  Date getFailedDate ();
  
  long getExecutionTime ();
  
}
