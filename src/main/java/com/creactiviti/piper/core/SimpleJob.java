package com.creactiviti.piper.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.util.Assert;

public class SimpleJob implements Job {

  private final String id = UUID.randomUUID().toString();
  private final Pipeline pipeline;
  private JobStatus status = JobStatus.CREATED;
  private final List<Task> tasks = new ArrayList<>();
  private int currentTask = 0 ;
  
  public SimpleJob (Pipeline aPipeline) {
    Assert.notNull(aPipeline,"pipeline must not be null");
    pipeline = aPipeline;
  }
  
  @Override
  public String getId() {
    return id;
  }

  @Override
  public List<Task> getTasks() {
    return tasks;
  }
  
  @Override
  public boolean hasMoreTasks() {
    return currentTask < tasks.size();
  }
  
  @Override
  public Task nextTask() {
    return pipeline.getTasks().get(currentTask);
  }
  
  @Override
  public JobStatus getStatus() {
    return status;
  }
  
  public void setStatus (JobStatus aStatus) {
    if(aStatus == JobStatus.COMPLETED) {
      Assert.isTrue(status==JobStatus.STARTED,String.format("Job %s is %s and so can not be COMPLETED", id,status));
      status = JobStatus.COMPLETED;
    }
    else if (aStatus == JobStatus.STARTED) {
      Assert.isTrue(status==JobStatus.CREATED||status==JobStatus.FAILED||status==JobStatus.STOPPED,String.format("Job %s is %s and so can not be STARTED", id,status));
      status = JobStatus.STARTED;
    }
    else {
      throw new IllegalArgumentException("Can't handle status: " + aStatus);
    }
  }
  
}
