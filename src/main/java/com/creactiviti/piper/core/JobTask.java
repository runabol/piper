package com.creactiviti.piper.core;

public interface JobTask extends Task {

  String getId ();
  
  String getStatus ();
  
  Object getOutput ();
  
}
