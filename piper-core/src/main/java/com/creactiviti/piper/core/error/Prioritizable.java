
package com.creactiviti.piper.core.error;

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
