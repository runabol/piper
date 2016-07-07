package com.creactiviti.piper.core.pipeline;

import java.util.List;

import com.creactiviti.piper.core.Task;

/**
 * Pipelines are the the blueprints that describe
 * the execution of a job.
 * 
 * @author Arik Cohen
 * @since Jun 12, 2016
 */
public interface Pipeline {

  /**
   * Returns the unique identifier of the pipeline. 
   */
  String getId ();

  /**
   * Returns a descriptive name for the pipeline. 
   */
  String getName ();
  
  /**
   * Returns the tasks that make up the pipeline.
   */
  List<Task> getTasks ();

}
