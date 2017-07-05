package com.creactiviti.piper.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.creactiviti.piper.core.event.EventListener;
import com.creactiviti.piper.core.event.EventListenerChain;
import com.creactiviti.piper.core.event.LogEventListener;

@Configuration
public class EventsConfiguration {

  @Bean
  @Primary
  EventListenerChain eventListener (List<EventListener> aEventListeners) {
    return new EventListenerChain(aEventListeners);
  }
  
  @Bean
  LogEventListener logEventListener () {
    return new LogEventListener();
  }
}
