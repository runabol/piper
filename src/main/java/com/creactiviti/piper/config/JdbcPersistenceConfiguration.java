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

import com.creactiviti.piper.core.context.SimpleContextRepository;
import com.creactiviti.piper.core.job.JdbcJobRepository;

@Configuration
@ConditionalOnProperty(name="piper.persistence.provider",havingValue="jdbc")
public class JdbcPersistenceConfiguration {

  @Bean
  JdbcJobRepository jdbcJobRepository () {
    return new JdbcJobRepository();
  }
  
  @Bean
  SimpleContextRepository simpleContextRepository () {
    return new SimpleContextRepository();
  }
  
}
