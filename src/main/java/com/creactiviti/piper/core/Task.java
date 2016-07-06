package com.creactiviti.piper.core;

public interface Task extends Accessor {

  String getId ();
  
  String getJobId ();
  
  String getHandler ();
  
  String getName ();
  
  String getNode ();
  
  String getReturns ();
  
  TaskStatus getTaskStatus ();
  
  Object getOutput ();
  
  <T> T getOutput (Class<T> aType);

}
