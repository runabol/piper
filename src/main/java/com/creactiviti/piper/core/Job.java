package com.creactiviti.piper.core;

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
   * Returns the {@link Pipeline} instance 
   * on which the job is based. 
   */
  Pipeline getPipeline ();

  JobStatus getStatus ();
  
  void complete ();
}
