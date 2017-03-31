/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import com.creactiviti.piper.core.context.Context;

/**
 * Strategy interface for evaluating a JobTask.
 *
 * @author Arik Cohen
 * @since Mar 31, 2017
 */
public interface TaskEvaluator {

  /**
   * Evaluate the {@link JobTask}
   * 
   * @param aJobTask
   *          The {@link JobTask} instance to evaluate
   * @param aContext
   *          The context to evaluate the task against
   * @return the evaluate {@link JobTask}.
   */
  JobTask evaluate (JobTask aJobTask, Context aContext);
  
}
