/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.error;

/**
 * An interface which indiciates that a message
 * object can be retried.
 * 
 * @author Arik Cohen
 * @since Apr 10, 2017
 */
public interface Retryable {

  /**
   * Defines the maximum number of times that 
   * this message may be retries. 
   * 
   * @return int the maximum number of retries. 
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
   * retry. Values are to be specified using 
   * the ISO-8601 format (excluding the PT prefix).
   * e.g. 10s (ten seconds), 1m (one minute) etc. 
   * 
   * Default: 1s
   * 
   * @return int the delay (in ms) to introduce
   * @see https://en.wikipedia.org/wiki/ISO_8601#Durations
   */
  String getRetryDelay ();
  
  /**
   * Returns the calculated retry delay. 
   * i.e.: delay * retryAttempts * retryDelayFactor
   * 
   * @return long the delay (in ms) to introduce
   */
  long getRetryDelayMillis ();
  
  /**
   * The factor to use in order to calculate
   * the actual delay time between each 
   * successive retry -- multiplying
   * by the value of {@link #getRetryDelay()}.
   * 
   * Default: 2
   * 
   * @return int the retry delay factor
   */
  int getRetryDelayFactor ();
}
