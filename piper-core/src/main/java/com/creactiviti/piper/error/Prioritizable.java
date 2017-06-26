/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Jun 2017
 */
package com.creactiviti.piper.error;

/**
 * 
 * @author Arik Cohen
 * @since Jun 2, 2017
 */
public interface Prioritizable {

  public static final int DEFAULT_PRIORITY = 0;
  
  
  /**
   * Retrives the priority value
   * 
   * @return int
   */
  int getPriority ();
  
}
