/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
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
   * Return the job's pipeline id.
   * 
   * @return {@link Pipeline} 
   */
  String getPipelineId ();
  
  /**
   * Return the job's human-readable name.
   * 
   * @return {@link Pipeline} 
   */
  String getName ();

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
   * Return the date of when the job failed.
   * 
   * @return {@link Date}
   */
  Date getFailedDate ();
  
}
