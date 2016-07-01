package com.creactiviti.piper.core;

import java.util.List;

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

  /**
   * Determines if more tasks are available to execute
   * on this pipeline.
   * 
   * @return boolean
   *           <code>true</code> if more tasks remain
   *           to execute on this pipeline. Otherwise
   *           <code>false</code>.
   */
  boolean hasNextTask ();

  /**
   * Returns the next task to execute on the pipeline.
   * 
   * @return {@link Task} the next task to execute 
   *         on the pipeline
   */
  Task nextTask ();

}
