/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, May 2017
 */
package com.creactiviti.piper.core.task;

/**
 * A repository that can be used to atomically set 
 * a counter value.
 *
 * @author Arik Cohen
 */
public interface CounterRepository {
  
  /**
   * Set the counter to the give value.
   * @param aCounterName the name of the counter
   * @param aValue the value to set the counter to.
   */
  void set (String aCounterName, long aValue);
  
  /**
   * Decrement the specified counter by 1.
   * @param aCounterName the name of the counter
   * @return the new value
   */
  long decrement (String aCounterName);

  /**
   * Delete the specified counter.
   * @param aCounterName the name of the counter
   */
  void delete (String aCounterName);
  
}
