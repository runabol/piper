
package com.creactiviti.piper.core.event;


/**
 * 
 * @author Arik Cohen
 * @since Jun 9, 2017
 */
public interface EventListener {

  void onApplicationEvent (PiperEvent aEvent);
  
}
