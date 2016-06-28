package com.creactiviti.piper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.HornetQMessenger;

@Configuration
public class HornetQMessengerConfiguration {

  @Bean
  HornetQMessenger hornetQMessenger () {
    return new HornetQMessenger();
  }


}
