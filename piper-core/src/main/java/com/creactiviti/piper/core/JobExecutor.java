package com.creactiviti.piper.core;

import com.creactiviti.piper.core.job.Job;


/**
 * 
 * @author Arik Cohen
 * @since Apr 24, 2017
 */
public interface JobExecutor {

  void execute (Job aJob);
  
}
