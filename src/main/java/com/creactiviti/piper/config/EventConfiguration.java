package com.creactiviti.piper.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.event.DistributedEventPublisher;
import com.creactiviti.piper.core.event.InternalEventPublisher;
import com.creactiviti.piper.core.messenger.Messenger;

@Configuration
public class EventConfiguration {

  @Bean
  @ConditionalOnWorker
  DistributedEventPublisher distributedEventPublisher (Messenger aMessenger) {
    return new DistributedEventPublisher(aMessenger);
  }

  @Bean
  @ConditionalOnCoordinator
  public InternalEventPublisher internalEventPublisher (ApplicationEventPublisher aApplicationEventPublisher) {
    return new InternalEventPublisher (aApplicationEventPublisher);
  }
  
}
