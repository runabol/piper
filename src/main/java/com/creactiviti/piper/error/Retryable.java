/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.error;

/**
 * An interface which indiciates that a message
 * object can be retried.
 * 
 * @author Arik Cohen
 * @since Apr 10, 2017
 */
public interface Retryable {

  /**
   * The retry value or how many times
   * can this message be retried. 
   * 
   * @return int the number of allows retries. 
   */
  int getRetry ();
  
  /**
   *  The number of times that this message 
   *  has been retried.
   * 
   * @return int the number attempted retries. 
   */
  int getRetryAttempts ();
  
  /**
   * The delay to introduce between each 
   * retry.
   * 
   * @return long the delay (in ms) to introduce
   */
  int getRetryDelay ();
  
}
