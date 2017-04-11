/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.creactiviti.piper.core.context.JdbcContextRepository;
import com.creactiviti.piper.core.job.JdbcJobRepository;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobStatus;
import com.creactiviti.piper.core.messenger.Queues;
import com.creactiviti.piper.core.messenger.SynchMessenger;
import com.creactiviti.piper.core.pipeline.FileSystemPipelineRepository;
import com.creactiviti.piper.core.task.DefaultTaskExecutor;
import com.creactiviti.piper.core.task.JdbcJobTaskRepository;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.TaskHandler;
import com.creactiviti.piper.taskhandler.io.Print;
import com.creactiviti.piper.taskhandler.time.Sleep;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CoordinatorTests {

  @Autowired
  private DataSource dataSource;
  
  @Autowired
  private ApplicationEventPublisher eventPublisher;
  
  @Test
  public void testStartJob () throws SQLException {
    Worker worker = new Worker();
    Coordinator coordinator = new Coordinator ();
   
    SynchMessenger workerMessenger = new SynchMessenger();
    workerMessenger.receive(Queues.COMPLETIONS, (o)->coordinator.completeTask((JobTask)o));
    workerMessenger.receive(Queues.EVENTS, (o)->coordinator.on(o));
    
    worker.setMessenger(workerMessenger);
    DefaultTaskHandlerResolver taskHandlerResolver = new DefaultTaskHandlerResolver();
    
    Map<String,TaskHandler<?>> handlers = new HashMap<>();
    handlers.put("print", new Print());
    handlers.put("sleep", new Sleep());
    
    taskHandlerResolver.setTaskHandlers(handlers);
    
    worker.setTaskHandlerResolver(taskHandlerResolver);
    
    ObjectMapper objectMapper = createObjectMapper();
    
    JdbcContextRepository contextRepository = new JdbcContextRepository();
    contextRepository.setJdbcTemplate(new JdbcTemplate(dataSource));
    contextRepository.setObjectMapper(objectMapper);
    
    coordinator.setContextRepository(contextRepository);
    
    JdbcJobTaskRepository taskRepository = new JdbcJobTaskRepository();
    taskRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    taskRepository.setObjectMapper(createObjectMapper());
    
    JdbcJobRepository jobRepository = new JdbcJobRepository();
    jobRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    jobRepository.setObjectMapper(objectMapper);
    jobRepository.setJobTaskRepository(taskRepository);
    
    coordinator.setJobRepository(jobRepository);
    coordinator.setPipelineRepository(new FileSystemPipelineRepository());
    coordinator.setJobTaskRepository(taskRepository);
    
    SynchMessenger coordinatorMessenger = new SynchMessenger();
    coordinatorMessenger.receive(Queues.TASKS, (o)->worker.handle((JobTask)o));
    DefaultTaskExecutor taskExecutor = new DefaultTaskExecutor();
    taskExecutor.setMessenger(coordinatorMessenger);
    coordinator.setTaskExecutor(taskExecutor);
    coordinator.setEventPublisher(eventPublisher);
        
    Job job = coordinator.start(MapObject.of(ImmutableMap.of("pipeline","demo/hello","name","me")));
    
    Job completedJob = jobRepository.findOne(job.getId());
    
    Assert.assertEquals(JobStatus.COMPLETED, completedJob.getStatus());
  }

  private ObjectMapper createObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ"));
    return objectMapper;
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testRequiredParams () {
    Coordinator coordinator = new Coordinator ();
    coordinator.setPipelineRepository(new FileSystemPipelineRepository());
    coordinator.start(MapObject.of(Collections.singletonMap("pipeline","demo/hello")));
  }
  
}
