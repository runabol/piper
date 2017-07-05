
package com.creactiviti.piper.core.error;

/**
 * An interface which marks an object as being
 * able to provide {@link Error} status about 
 * itself.
 * 
 * @author Arik Cohen
 * @since Apr 10, 2017
 */
public interface Errorable {

  
  /**
   * Returns the error associated with the object.
   */
  Error getError ();

}
