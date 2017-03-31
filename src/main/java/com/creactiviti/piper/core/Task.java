package com.creactiviti.piper.core;

public interface Task extends Accessor {

  String getType ();
  
  String getName ();
  
  String getLabel ();
  
  String getNode ();
  
}
