package com.creactiviti.piper.core;

import java.util.List;

public interface Pipeline {

  String getId ();

  String getName ();
  
  List<Task> getTasks ();

  boolean isActive ();
  
}
