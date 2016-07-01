package com.creactiviti.piper.core;

import java.util.List;

public interface JobRepository {
  
  List<Job> findAll ();
  
  Job find (String aId);
  
  Job save (Job aJob);
  
}
