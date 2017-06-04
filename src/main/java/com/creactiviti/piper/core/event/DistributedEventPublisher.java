/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Jun 2017
 */
package com.creactiviti.piper.core.event;

import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.messenger.Queues;

/**
 * @author Arik Cohen
 * @since Jun 4, 2017
 */
public class DistributedEventPublisher implements EventPublisher {

  private final Messenger messenger;
  
  public DistributedEventPublisher (Messenger aMessenger) {
    messenger = aMessenger;
  }
  
  @Override
  public void publishEvent(PiperEvent aEvent) {
    messenger.send(Queues.EVENTS, aEvent);
  }

}
