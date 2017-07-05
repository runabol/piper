
package com.creactiviti.piper.core.cache;


/**
 * Allows a class that holds (typically temporary, cache, etc.) state
 * to be cleared on demand.
 * 
 * @author Arik Cohen
 * @since Mar 28, 2017
 */
public interface Clearable {

  void clear ();
  
}
