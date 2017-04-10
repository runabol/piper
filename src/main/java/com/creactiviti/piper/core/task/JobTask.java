/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Date;

import com.creactiviti.piper.error.Errorable;

public interface JobTask extends Task, Errorable {

  String getId ();
  
  String getJobId ();
  
  TaskStatus getStatus ();
  
  Object getOutput ();
  
  Date getCreationDate ();
  
  Date getCompletionDate ();
  
  Date getFailedDate ();
  
  long getExecutionTime ();
  
}
