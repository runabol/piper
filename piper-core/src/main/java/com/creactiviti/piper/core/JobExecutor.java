/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
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
