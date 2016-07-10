package com.creactiviti.piper;

import java.util.Collections;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.creactiviti.piper.core.DefaultCoordinator;
import com.creactiviti.piper.core.DefaultWorker;
import com.creactiviti.piper.core.context.SimpleContextRepository;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobStatus;
import com.creactiviti.piper.core.job.SimpleJobRepository;
import com.creactiviti.piper.core.messenger.SimpleMessenger;
import com.creactiviti.piper.core.pipeline.YamlPipelineRepository;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.taskhandler.io.Log;

public class DefaultCoordinatorTests {

  @Test
  public void testStartJob () {
    
    DefaultWorker worker = new DefaultWorker();
    DefaultCoordinator coordinator = new DefaultCoordinator ();
    
    worker.setMessenger(new SimpleMessenger("completions", (o)->coordinator.complete((JobTask)o)));
    worker.setTaskHandlers(Collections.singletonMap("log", new Log()));
    
    coordinator.setContextRepository(new SimpleContextRepository());
    coordinator.setJobRepository(new SimpleJobRepository());
    coordinator.setPipelineRepository(new YamlPipelineRepository());
    coordinator.setMessenger(new SimpleMessenger("tasks", (o)->worker.handle((JobTask)o)));
        
    Job job = coordinator.start("demo/hello", new HashMap<String, Object> ());
    
    Assert.assertEquals(JobStatus.COMPLETED, job.getStatus());
  }
  
}
