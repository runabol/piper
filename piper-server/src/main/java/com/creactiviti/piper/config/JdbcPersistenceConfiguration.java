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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.creactiviti.piper.core.context.JdbcContextRepository;
import com.creactiviti.piper.core.job.JdbcJobRepository;
import com.creactiviti.piper.core.task.CounterRepository;
import com.creactiviti.piper.core.task.JdbcCounterRepository;
import com.creactiviti.piper.core.task.JdbcTaskExecutionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ConditionalOnProperty(name="piper.persistence.provider",havingValue="jdbc")
public class JdbcPersistenceConfiguration {

  @Bean
  JdbcTaskExecutionRepository jdbcJobTaskRepository (NamedParameterJdbcTemplate aJdbcTemplate, ObjectMapper aObjectMapper) {
    JdbcTaskExecutionRepository jdbcJobTaskRepository = new JdbcTaskExecutionRepository();
    jdbcJobTaskRepository.setJdbcOperations(aJdbcTemplate);
    jdbcJobTaskRepository.setObjectMapper(aObjectMapper);
    return jdbcJobTaskRepository;
  }
  
  @Bean
  JdbcJobRepository jdbcJobRepository (NamedParameterJdbcTemplate aJdbcTemplate, ObjectMapper aObjectMapper) {
    JdbcJobRepository jdbcJobRepository = new JdbcJobRepository();
    jdbcJobRepository.setJdbcOperations(aJdbcTemplate);
    jdbcJobRepository.setJobTaskRepository(jdbcJobTaskRepository(aJdbcTemplate,aObjectMapper));
    return jdbcJobRepository;
  }
  
  @Bean
  JdbcContextRepository jdbcContextRepository (JdbcTemplate aJdbcTemplate, ObjectMapper aObjectMapper) {
    JdbcContextRepository repo = new JdbcContextRepository();
    repo.setJdbcTemplate(aJdbcTemplate);
    repo.setObjectMapper(aObjectMapper);
    return repo;
  }
  
  @Bean
  CounterRepository counterRepository (JdbcTemplate aJdbcOperations) {
    return new JdbcCounterRepository(aJdbcOperations);
  }
  
}
