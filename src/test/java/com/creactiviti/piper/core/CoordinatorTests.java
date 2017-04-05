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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.creactiviti.piper.core.context.InMemoryContextRepository;
import com.creactiviti.piper.core.job.JdbcJobRepository;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobStatus;
import com.creactiviti.piper.core.messenger.SynchMessenger;
import com.creactiviti.piper.core.pipeline.FileSystemPipelineRepository;
import com.creactiviti.piper.core.task.DefaultTaskExecutor;
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
  
  @Test
  public void testStartJob () throws SQLException {
    Worker worker = new Worker();
    Coordinator coordinator = new Coordinator ();
   
    SynchMessenger workerMessenger = new SynchMessenger();
    workerMessenger.receive("coordinator.completions", (o)->coordinator.completeTask((JobTask)o));
    worker.setMessenger(workerMessenger);
    DefaultTaskHandlerResolver taskHandlerResolver = new DefaultTaskHandlerResolver();
    
    Map<String,TaskHandler<?>> handlers = new HashMap<>();
    handlers.put("print", new Print());
    handlers.put("sleep", new Sleep());
    
    taskHandlerResolver.setTaskHandlers(handlers);
    
    worker.setTaskHandlerResolver(taskHandlerResolver);
    
    ObjectMapper objectMapper = createObjectMapper();
    
    coordinator.setContextRepository(new InMemoryContextRepository());
    JdbcJobRepository jobRepository = new JdbcJobRepository();
    jobRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    jobRepository.setObjectMapper(objectMapper);
    
    coordinator.setJobRepository(jobRepository);
    coordinator.setPipelineRepository(new FileSystemPipelineRepository());
    
    SynchMessenger coordinatorMessenger = new SynchMessenger();
    coordinatorMessenger.receive("worker.tasks", (o)->worker.handle((JobTask)o));
    DefaultTaskExecutor taskExecutor = new DefaultTaskExecutor();
    taskExecutor.setMessenger(coordinatorMessenger);
    coordinator.setTaskExecutor(taskExecutor);
        
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
