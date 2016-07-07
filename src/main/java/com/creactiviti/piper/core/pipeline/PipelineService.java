package com.creactiviti.piper.core.pipeline;

import java.util.List;


/**
 * 
 * @author Arik Cohen
 * @since Jun 18, 2016
 */
public interface PipelineService {
  
  Pipeline create (String aId);
  
  List<Pipeline> list ();

}
