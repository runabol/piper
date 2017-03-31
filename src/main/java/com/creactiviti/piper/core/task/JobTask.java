/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Date;

public interface JobTask extends Task {

  String getId ();
  
  TaskStatus getStatus ();
  
  Object getOutput ();
  
  Exception getException ();
  
  Date getCreationDate ();
  
  Date getCompletionDate ();
  
  Date getFailedDate ();
  
  long getExecutionTime ();
  
}
