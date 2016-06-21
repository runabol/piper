package com.creactiviti.piper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.LocalMessenger;

@Configuration
public class LocalMessengerConfiguration {
  
  @Value("piper.node.roles")
  private String roles;
  
  @Bean
  LocalMessenger localMessenger() {
    return new LocalMessenger ();
  }
  

}
