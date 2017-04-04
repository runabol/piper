/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */

package com.creactiviti.piper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.context.Context;
import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.pipeline.PipelineRepository;
import com.creactiviti.piper.core.task.SpelTaskEvaluator;
import com.creactiviti.piper.core.task.TaskExecutor;

@Configuration
@ConditionalOnCoordinator
public class CoordinatorConfiguration {

  @Autowired
  private ApplicationEventPublisher eventPublisher;
  
  @Autowired
  private ContextRepository<Context> contextRepository;
  
  @Autowired
  private JobRepository jobRepository;
  
  @Bean
  Coordinator coordinator (PipelineRepository aPipelineRepository, TaskExecutor aTaskExecutor) {
    Coordinator coordinator = new Coordinator();
    coordinator.setContextRepository(contextRepository);
    coordinator.setEventPublisher(eventPublisher);
    coordinator.setJobRepository(jobRepository);
    coordinator.setPipelineRepository(aPipelineRepository);
    coordinator.setTaskEvaluator(new SpelTaskEvaluator());
    coordinator.setTaskExecutor(aTaskExecutor);
    return coordinator;
  }
  
}
