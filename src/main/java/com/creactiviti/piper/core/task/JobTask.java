package com.creactiviti.piper.core.task;

import java.util.Date;

import com.creactiviti.piper.core.Task;

public interface JobTask extends Task {

  String getId ();
  
  String getStatus ();
  
  Object getOutput ();
  
  Date getCreationDate ();
  
  Date getCompletionDate ();
  
  Date getFailedDate ();
  
}
