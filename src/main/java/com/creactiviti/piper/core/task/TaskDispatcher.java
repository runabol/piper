/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

/**
 * A strategy interface for dispatching {@link JobTask}
 * instances to be executed.
 * 
 * @author Arik Cohen
 * @since Mar 26, 2017
 */
public interface TaskDispatcher<T extends Task> {

  /**
   * Dispatches a {@link JobTask} instance.
   * 
   * @param aTask
   *          The task to dispatch
   */
  void dispatch (T aTask);

}
