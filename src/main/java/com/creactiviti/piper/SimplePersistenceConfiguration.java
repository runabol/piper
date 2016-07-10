package com.creactiviti.piper;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.context.SimpleContextRepository;
import com.creactiviti.piper.core.job.SimpleJobRepository;

@Configuration
@ConditionalOnProperty(name="piper.persistence.provider",havingValue="simple")
public class SimplePersistenceConfiguration {

  @Bean
  SimpleJobRepository simpleJobRepository () {
    return new SimpleJobRepository();
  }
  
  @Bean
  SimpleContextRepository simpleContextRepository () {
    return new SimpleContextRepository();
  }
  
}
