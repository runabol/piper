/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.error;

/**
 * 
 * @author Arik Cohen
 * @since Apr 10, 2017
 */
public interface Error {

  /**
   * Returns the detail message string of this error.
   *
   * @return  the detail message string of this {@code Error} instance
   *          (which may be {@code null}).
   */
  String getMessage ();
  
  /**
   * Provides programmatic access to the stack trace information.  
   * Returns an array of stack trace elements,
   * each representing one stack frame.  The zeroth element of the array
   * (assuming the array's length is non-zero) represents the top of the
   * stack, which is the last method invocation in the sequence.  Typically,
   * this is the point at which this throwable was created and thrown.
   * The last element of the array (assuming the array's length is non-zero)
   * represents the bottom of the stack, which is the first method invocation
   * in the sequence.
   *
   * @return an array of stack trace elements representing the stack trace
   *         pertaining to this error.
   */
  String[] getStackTrace ();
  
}
