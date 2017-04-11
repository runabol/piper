/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Date;

import com.creactiviti.piper.error.Errorable;
import com.creactiviti.piper.error.Retryable;

public interface JobTask extends PipelineTask, Errorable, Retryable {

  String getId ();
  
  String getJobId ();
  
  TaskStatus getStatus ();
  
  Object getOutput ();
  
  Date getCreationDate ();
  
  Date getCompletionDate ();

  Date getCancellationDate ();
  
  Date getFailedDate ();
  
  long getExecutionTime ();
  
}
