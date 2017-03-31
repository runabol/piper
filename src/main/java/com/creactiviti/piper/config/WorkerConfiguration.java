package com.creactiviti.piper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.Worker;

@Configuration
public class WorkerConfiguration {

  @Bean
  Worker worker () {
    return new Worker();
  }
  
}
