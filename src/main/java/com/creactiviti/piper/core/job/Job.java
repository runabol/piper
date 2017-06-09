/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.job;

import java.util.Date;
import java.util.List;

import com.creactiviti.piper.core.Accessor;
import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.task.TaskExecution;
import com.creactiviti.piper.error.Errorable;
import com.creactiviti.piper.error.Prioritizable;

/**
 * Represents an instance of a job.
 * 
 * @author Arik Cohen
 * @since Jun 12, 2016
 */
public interface Job extends Errorable, Prioritizable {

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
   * Returns the index of the step on the job's 
   * pipeline on which the job is working on right 
   * now.
   * 
   * @return int
   *           The step ordinal number
   */
  int getCurrentTask ();
  
  /**
   * Returns the list of tasks that were executed as part
   * of the job instance.
   * 
   * @return {@link List}
   */
  List<TaskExecution> getExecution ();

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
  String getLabel ();

  /**
   * Return the time when the job was originally 
   * created.
   * 
   * @return {@link Date}
   */
  Date getCreateTime ();
    
  /**
   * Return the time of when the job began 
   * execution.
   * 
   * @return {@link Date}
   */
  Date getStartTime ();  
  
  /**
   * Get time execution entered end status: COMPLETED, STOPPED, FAILED 
   * 
   * @return {@link Date}
   */
  Date getEndTime ();
  
  /**
   * Get the list of tags assigned to the job.
   * 
   * @return String[]
   */
  String[] getTags ();

  /**
   * Get the key-value map of inputs passed 
   * to the job when it was created.
   * 
   * @return {@link Accessor}
   */
  Accessor getInputs ();

  /**
   * Get the list of webhooks configured
   * for this job.
   * 
   * @return {@link List}
   */
  List<Accessor> getWebhooks ();
  
}
