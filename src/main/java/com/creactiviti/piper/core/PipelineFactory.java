package com.creactiviti.piper.core;

import java.util.List;


/**
 * <p>The core interface for creating {@link Pipeline} objects.</p>
 * 
 * @author Arik Cohen
 * @since Jun 18, 2016
 */
public interface PipelineFactory {
  
  Pipeline createPipeline (String aId);
  
  List<Pipeline> createPipelines ();

}
