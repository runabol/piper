/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.JobExecutor;
import com.creactiviti.piper.core.TaskCompletionHandler;
import com.creactiviti.piper.core.context.Context;
import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.pipeline.PipelineRepository;
import com.creactiviti.piper.core.task.JobTaskRepository;
import com.creactiviti.piper.core.task.TaskDispatcher;
import com.creactiviti.piper.error.ErrorHandler;
import com.creactiviti.piper.error.ErrorHandlerChain;
import com.creactiviti.piper.error.JobTaskErrorHandler;

@Configuration
@ConditionalOnCoordinator
public class CoordinatorConfiguration {

  @Autowired private JobRepository jobRepository;
  @Autowired private JobTaskRepository jobTaskRepository;
  @Autowired private ContextRepository<Context> contextRepository;
  @Autowired private ApplicationEventPublisher eventPublisher;
  @Autowired private PipelineRepository pipelineRepository;
  @Autowired private TaskDispatcher taskDispatcher;
  @Autowired private TaskCompletionHandler taskCompletionHandler;
  @Autowired private JobExecutor jobExecutor;
  
  @Bean
  Coordinator coordinator () {
    Coordinator coordinator = new Coordinator();
    coordinator.setContextRepository(contextRepository);
    coordinator.setEventPublisher(eventPublisher);
    coordinator.setJobRepository(jobRepository);
    coordinator.setJobTaskRepository(jobTaskRepository);
    coordinator.setPipelineRepository(pipelineRepository);
    coordinator.setJobExecutor(jobExecutor);
    coordinator.setTaskDispatcher(taskDispatcher);
    coordinator.setErrorHandler(errorHandler());
    coordinator.setTaskCompletionHandler(taskCompletionHandler);
    return coordinator;
  }
  

  @Bean
  ErrorHandler errorHandler () {
    return new ErrorHandlerChain(Arrays.asList(jobTaskErrorHandler()));
  }
  
  @Bean
  JobTaskErrorHandler jobTaskErrorHandler () {
    JobTaskErrorHandler jobTaskErrorHandler = new JobTaskErrorHandler();
    jobTaskErrorHandler.setJobRepository(jobRepository);
    jobTaskErrorHandler.setJobTaskRepository(jobTaskRepository);
    jobTaskErrorHandler.setTaskDispatcher(taskDispatcher);
    return jobTaskErrorHandler;
  }
  
}
