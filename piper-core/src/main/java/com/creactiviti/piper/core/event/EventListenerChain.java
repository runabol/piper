package com.creactiviti.piper.core.event;

import java.util.List;

/**
 * 
 * @author Arik Cohen
 * @since Jul 5, 2017
 */
public class EventListenerChain implements EventListener {

  private final List<EventListener> listeners;
  
  public EventListenerChain(List<EventListener> aEventListeners) {
    listeners = aEventListeners;
  }
  
  @Override
  public void onApplicationEvent(PiperEvent aEvent) {
    for(EventListener listener : listeners) {
      listener.onApplicationEvent(aEvent);
    }
  }

}
