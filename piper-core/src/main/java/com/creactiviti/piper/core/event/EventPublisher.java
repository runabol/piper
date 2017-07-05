
package com.creactiviti.piper.core.event;

/**
 * 
 * @author Arik Cohen
 * @since Jun 4, 2017
 */
public interface EventPublisher {

  /**
   * Notify all <strong>matching</strong> listeners registered with this
   * application of an application event. 
   * @param event the event to publish
   */
  void publishEvent (PiperEvent event);
  
}
