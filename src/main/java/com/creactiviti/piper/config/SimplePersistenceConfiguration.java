/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.context.InMemoryContextRepository;
import com.creactiviti.piper.core.job.InMemoryJobRepository;

@Configuration
@ConditionalOnProperty(name="piper.persistence.provider",havingValue="simple")
public class SimplePersistenceConfiguration {

  @Bean
  InMemoryJobRepository simpleJobRepository () {
    return new InMemoryJobRepository();
  }
  
  @Bean
  InMemoryContextRepository simpleContextRepository () {
    return new InMemoryContextRepository();
  }
  
}
