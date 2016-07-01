package com.creactiviti.piper.core;

import java.util.List;

public interface JobRepository {
  
  List<MutableJob> findAll ();
  
  MutableJob find (String aId);
  
  MutableJob save (MutableJob aJob);
  
}
