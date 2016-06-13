package com.creactiviti.piper.core;

import java.util.List;

public interface PipelineRepository {
  
  List<Pipeline> findAll ();
  
  Pipeline find (String aId);

}
