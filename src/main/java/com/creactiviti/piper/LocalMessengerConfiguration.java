package com.creactiviti.piper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.LocalMessenger;
import com.creactiviti.piper.core.Task;
import com.creactiviti.piper.core.Worker;

@Configuration
@EnableConfigurationProperties({CoordinatorProperties.class,WorkerProperties.class})
public class LocalMessengerConfiguration {

  @Autowired
  private Coordinator coordinator;
  
  @Autowired
  private Worker worker;
  
  @Bean
  LocalMessenger localMessenger (CoordinatorProperties aCoordinatorProperties, WorkerProperties aWorkerProperties) {
    LocalMessenger localMessenger = new LocalMessenger ();
    localMessenger.receive("completions", (m)->coordinator.complete((Task) m));
    localMessenger.receive("errors", (m)->coordinator.error((Task)m));
    return localMessenger;
  }
  

}
