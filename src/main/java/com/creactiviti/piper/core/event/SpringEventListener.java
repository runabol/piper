/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Jun 2017
 */
package com.creactiviti.piper.core.event;

import org.springframework.context.ApplicationEventPublisher;

/**
 * 
 * @author Arik Cohen
 * @since Jun 9, 2017
 */
public class SpringEventListener implements EventListener {

  private final ApplicationEventPublisher eventPublisher;
  
  public SpringEventListener(ApplicationEventPublisher aEventPublisher) {
    eventPublisher = aEventPublisher;
  }
  
  @Override
  public void onApplicationEvent (PiperEvent aEvent) {
    eventPublisher.publishEvent(aEvent);
  }
  
}
