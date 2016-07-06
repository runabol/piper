package com.creactiviti.piper.core;

import java.util.List;

/**
 * Represents an instance of a job.
 * 
 * @author Arik Cohen
 * @since Jun 12, 2016
 */
public interface Job {

  /**
   * Return the ID of the job.
   */
  String getId ();
  
  /**
   * Return the {@link JobStatus}
   * 
   * @return The job's status.
   */
  JobStatus getStatus ();
  
  /**
   * Returns the list of tasks of the job.
   * 
   * @return {@link List}
   */
  List<Task> getTasks ();
  
  /**
   * Determines if more tasks are available to execute
   * on this job.
   * 
   * @return boolean
   *           <code>true</code> if more tasks remain
   *           to execute on this pipeline. Otherwise
   *           <code>false</code>.
   */
  boolean hasNextTask ();

  /**
   * Returns the next task to execute on the job.
   * 
   * @return {@link Task} the next task to execute 
   *         on the pipeline
   */
  Task nextTask ();
  
}
