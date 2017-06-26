/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.error;

/**
 * 
 * A strategy for handling errors. This is especially useful for handling
 * errors that occur during asynchronous execution of tasks that have been
 * submitted to a worker. In such cases, it may not be possible to
 * throw the error to the original caller.
 *
 * @author Arik Cohen
 * @since Apt 10, 2017
 */
public interface ErrorHandler<E extends Errorable> {

  /**
   * Handle the given error.
   */
  void handle (E aErrorable);
  
}
