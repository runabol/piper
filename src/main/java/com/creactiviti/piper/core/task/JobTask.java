package com.creactiviti.piper.core.task;

import com.creactiviti.piper.core.Task;

public interface JobTask extends Task {

  String getId ();
  
  String getStatus ();
  
  Object getOutput ();
  
}
