package com.creactiviti.piper.core.task;

import java.util.Date;

import com.creactiviti.piper.core.Task;

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