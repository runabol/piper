package com.creactiviti.piper.core.job;

import java.util.Date;
import java.util.List;

import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.task.JobTask;

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
  List<JobTask> getExecution ();

  /**
   * Return the job's pipeline.
   * 
   * @return {@link Pipeline} 
   */
  Pipeline getPipeline ();

  /**
   * Return the date of when the job was originally 
   * created.
   * 
   * @return {@link Date}
   */
  Date getCreationDate ();
    
  /**
   * Return the date of when the job began 
   * execution.
   * 
   * @return {@link Date}
   */
  Date getStartDate();  
  
  /**
   * Return the date of when the job finished
   * successfully.
   * 
   * @return {@link Date}
   */
  Date getCompletionDate ();
  
  /**
   * Determines if more tasks are available to execute
   * on this job.
   * 
   * @return boolean
   *           <code>true</code> if more tasks remain
   *           to execute on this job. Otherwise
   *           <code>false</code>.
   */
  boolean hasMoreTasks ();

}
