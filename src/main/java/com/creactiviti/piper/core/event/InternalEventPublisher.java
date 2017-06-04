/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Jun 2017
 */
package com.creactiviti.piper.core.event;

import org.springframework.context.ApplicationEventPublisher;

/**
 * @author Arik Cohen
 * @since Jun 4, 2017
 */
public class InternalEventPublisher implements EventPublisher {

  private final ApplicationEventPublisher eventPulisher;
  
  public InternalEventPublisher (ApplicationEventPublisher aEventPulisher) {
    eventPulisher = aEventPulisher;
  }
  
  @Override
  public void publishEvent(PiperEvent aEvent) {
    eventPulisher.publishEvent(aEvent);
  }

}
