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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.creactiviti.piper.core.context.InMemoryContextRepository;
import com.creactiviti.piper.core.job.JdbcJobRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ConditionalOnProperty(name="piper.persistence.provider",havingValue="jdbc")
public class JdbcPersistenceConfiguration {

  @Bean
  JdbcJobRepository jdbcJobRepository (NamedParameterJdbcTemplate aJdbcTemplate, ObjectMapper aObjectMapper) {
    JdbcJobRepository jdbcJobRepository = new JdbcJobRepository();
    jdbcJobRepository.setJdbcOperations(aJdbcTemplate);
    jdbcJobRepository.setObjectMapper(aObjectMapper);
    return jdbcJobRepository;
  }
  
  @Bean
  InMemoryContextRepository simpleContextRepository () {
    return new InMemoryContextRepository();
  }
  
}
