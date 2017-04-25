/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core;

import com.creactiviti.piper.core.task.JobTask;

/**
 * A strategy interface for handling {@link JobTask}
 * completions.
 * 
 * @author Arik Cohen
 * @since Apr 23, 2017
 */
public interface TaskCompletionHandler {
  
  void handle (JobTask aJobTask);
  
  boolean canHandle (JobTask aJobTask);

}
