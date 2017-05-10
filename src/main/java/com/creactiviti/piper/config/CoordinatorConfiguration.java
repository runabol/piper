/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.DefaultJobExecutor;
import com.creactiviti.piper.core.DefaultTaskCompletionHandler;
import com.creactiviti.piper.core.EachTaskCompletionHandler;
import com.creactiviti.piper.core.TaskCompletionHandler;
import com.creactiviti.piper.core.TaskCompletionHandlerChain;
import com.creactiviti.piper.core.context.Context;
import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.event.TaskStartedEventHandler;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.pipeline.PipelineRepository;
import com.creactiviti.piper.core.task.ControlTaskDispatcher;
import com.creactiviti.piper.core.task.CounterRepository;
import com.creactiviti.piper.core.task.EachTaskDispatcher;
import com.creactiviti.piper.core.task.TaskDispatcher;
import com.creactiviti.piper.core.task.TaskDispatcherChain;
import com.creactiviti.piper.core.task.TaskDispatcherResolver;
import com.creactiviti.piper.core.task.TaskExecutionRepository;
import com.creactiviti.piper.core.task.WorkTaskDispatcher;
import com.creactiviti.piper.error.ErrorHandler;
import com.creactiviti.piper.error.ErrorHandlerChain;
import com.creactiviti.piper.error.TaskExecutionErrorHandler;

@Configuration
@ConditionalOnCoordinator
public class CoordinatorConfiguration {

  @Autowired private JobRepository jobRepository;
  @Autowired private TaskExecutionRepository jobTaskRepository;
  @Autowired private ContextRepository<Context> contextRepository;
  @Autowired private ApplicationEventPublisher eventPublisher;
  @Autowired private PipelineRepository pipelineRepository;
  @Autowired private CounterRepository counterRepository;
  @Autowired private Messenger messenger;
  
  @Bean
  Coordinator coordinator () {
    Coordinator coordinator = new Coordinator();
    coordinator.setContextRepository(contextRepository);
    coordinator.setEventPublisher(eventPublisher);
    coordinator.setJobRepository(jobRepository);
    coordinator.setJobTaskRepository(jobTaskRepository);
    coordinator.setPipelineRepository(pipelineRepository);
    coordinator.setJobExecutor(jobExecutor());
    coordinator.setTaskDispatcher(taskDispatcher());
    coordinator.setErrorHandler(errorHandler());
    coordinator.setTaskCompletionHandler(taskCompletionHandler());
    return coordinator;
  }
  
  @Bean
  ErrorHandler errorHandler () {
    return new ErrorHandlerChain(Arrays.asList(jobTaskErrorHandler()));
  }
  
  @Bean
  TaskExecutionErrorHandler jobTaskErrorHandler () {
    TaskExecutionErrorHandler jobTaskErrorHandler = new TaskExecutionErrorHandler();
    jobTaskErrorHandler.setJobRepository(jobRepository);
    jobTaskErrorHandler.setJobTaskRepository(jobTaskRepository);
    jobTaskErrorHandler.setTaskDispatcher(taskDispatcher());
    jobTaskErrorHandler.setEventPublisher(eventPublisher);
    return jobTaskErrorHandler;
  }
  
  @Bean
  TaskCompletionHandlerChain taskCompletionHandler () {
    TaskCompletionHandlerChain taskCompletionHandlerChain = new TaskCompletionHandlerChain();
    taskCompletionHandlerChain.setTaskCompletionHandlers(Arrays.asList(
      eachTaskCompletionHandler(taskCompletionHandlerChain),
      defaultTaskCompletionHandler()
    ));
    return taskCompletionHandlerChain;
  }
  
  @Bean
  DefaultTaskCompletionHandler defaultTaskCompletionHandler () {
    DefaultTaskCompletionHandler taskCompletionHandler = new DefaultTaskCompletionHandler();
    taskCompletionHandler.setContextRepository(contextRepository);
    taskCompletionHandler.setJobExecutor(jobExecutor());
    taskCompletionHandler.setJobRepository(jobRepository);
    taskCompletionHandler.setJobTaskRepository(jobTaskRepository);
    taskCompletionHandler.setPipelineRepository(pipelineRepository);
    taskCompletionHandler.setEventPublisher(eventPublisher);
    return taskCompletionHandler;
  }
  
  @Bean
  EachTaskCompletionHandler eachTaskCompletionHandler (TaskCompletionHandler aTaskCompletionHandler) {
    return new EachTaskCompletionHandler(jobTaskRepository,aTaskCompletionHandler,counterRepository);
  }
  
  @Bean
  DefaultJobExecutor jobExecutor () {
    DefaultJobExecutor jobExecutor = new DefaultJobExecutor();
    jobExecutor.setContextRepository(contextRepository);
    jobExecutor.setJobRepository(jobRepository);
    jobExecutor.setJobTaskRepository(jobTaskRepository);
    jobExecutor.setPipelineRepository(pipelineRepository);
    jobExecutor.setTaskDispatcher(taskDispatcher());
    return jobExecutor;
  }
  
  @Bean
  TaskDispatcherChain taskDispatcher () {
    TaskDispatcherChain tashDispatcher = new TaskDispatcherChain();
    List<TaskDispatcherResolver> resolvers =  Arrays.asList(
      eachTaskDispatcher(tashDispatcher),
      controlTaskDispatcher(),
      workTaskDispatcher()
    );
    tashDispatcher.setResolvers(resolvers);
    return tashDispatcher;
  }
  
  @Bean
  ControlTaskDispatcher controlTaskDispatcher () {
    return new ControlTaskDispatcher(messenger);
  }
  
  @Bean
  EachTaskDispatcher eachTaskDispatcher (TaskDispatcher aTaskDispatcher) {
    return new EachTaskDispatcher(aTaskDispatcher,jobTaskRepository,messenger,contextRepository,counterRepository);
  }
  
  @Bean
  WorkTaskDispatcher workTaskDispatcher () {
    return new WorkTaskDispatcher(messenger);
  }
  
  @Bean
  TaskStartedEventHandler taskStartedEventHandler () {
    return new TaskStartedEventHandler(jobTaskRepository, taskDispatcher());
  }
  
}
