/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.creactiviti.piper.core.context.InMemoryContextRepository;
import com.creactiviti.piper.core.job.InMemoryJobRepository;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobStatus;
import com.creactiviti.piper.core.messenger.SynchMessenger;
import com.creactiviti.piper.core.pipeline.FileSystemPipelineRepository;
import com.creactiviti.piper.core.task.DefaultTaskExecutor;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.TaskHandler;
import com.creactiviti.piper.taskhandler.io.Print;
import com.creactiviti.piper.taskhandler.time.Sleep;
import com.google.common.collect.ImmutableMap;

public class CoordinatorTests {

  @Test
  public void testStartJob () {
    
    Worker worker = new Worker();
    Coordinator coordinator = new Coordinator ();
   
    SynchMessenger workerMessenger = new SynchMessenger();
    workerMessenger.receive("completions", (o)->coordinator.completeTask((JobTask)o));
    worker.setMessenger(workerMessenger);
    DefaultTaskHandlerResolver taskHandlerResolver = new DefaultTaskHandlerResolver();
    
    Map<String,TaskHandler<?>> handlers = new HashMap<>();
    handlers.put("print", new Print());
    handlers.put("sleep", new Sleep());
    
    taskHandlerResolver.setTaskHandlers(handlers);
    
    worker.setTaskHandlerResolver(taskHandlerResolver);
    
    coordinator.setContextRepository(new InMemoryContextRepository());
    InMemoryJobRepository jobRepository = new InMemoryJobRepository();
    coordinator.setJobRepository(jobRepository);
    coordinator.setPipelineRepository(new FileSystemPipelineRepository());
    
    SynchMessenger coordinatorMessenger = new SynchMessenger();
    coordinatorMessenger.receive("tasks", (o)->worker.handle((JobTask)o));
    DefaultTaskExecutor taskExecutor = new DefaultTaskExecutor();
    taskExecutor.setMessenger(coordinatorMessenger);
    coordinator.setTaskExecutor(taskExecutor);
        
    Job job = coordinator.start(MapObject.of(ImmutableMap.of("pipeline","demo/hello","name","me")));
    
    Job completedJob = jobRepository.findOne(job.getId());
    
    Assert.assertEquals(JobStatus.COMPLETED, completedJob.getStatus());
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testRequiredParams () {
    Coordinator coordinator = new Coordinator ();
    coordinator.setPipelineRepository(new FileSystemPipelineRepository());
    coordinator.start(MapObject.of(Collections.singletonMap("pipeline","demo/hello")));
  }
  
}
