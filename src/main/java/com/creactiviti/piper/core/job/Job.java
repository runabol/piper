package com.creactiviti.piper.core.job;

import java.util.List;

import com.creactiviti.piper.core.JobStatus;
import com.creactiviti.piper.core.Task;

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
   * Returns the list of tasks that were executed as part
   * of the job instance.
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
  boolean hasMoreTasks ();

}
