package com.creactiviti.piper.core;

/**
 * The central interface responsible for coordinating 
 * and executing jobs.
 * 
 * @author Arik Cohen
 * @since Jun 12, 2016
 */
public interface Coordinator {

  /**
   * Starts a job instance.
   * 
   * @param aPipelineId
   *          The ID of the pipeline that will execute the job.
   * @return Job
   *           The instance of the Job
   */
  Job start (String aPipelineId);
  
  /**
   * Stop a running job.
   * 
   * @param aJobId
   *          The id of the job to stop
   */
  Job stop (String aJobId);

  /**
   * Resume a stopped or failed job.
   * 
   * @param aJobId
   *          The id of the job to resume.
   */
  Job resume (String aJobId);
  
  /**
   * Retrieve a Job instance by its ID. 
   * 
   * @param aJobId
   *          The ID of the job
   * @return The Job instance.
   */
  Job get (String aJobId);
  
  
}
