package com.creactiviti.piper.core;

import java.util.UUID;

import org.springframework.util.Assert;

public class SimpleJob implements Job {

  private final String id = UUID.randomUUID().toString();
  private final Pipeline pipeline;
  private JobStatus status;
  
  public SimpleJob (Pipeline aPipeline) {
    Assert.notNull(aPipeline,"pipeline must not be null");
    pipeline = aPipeline;
  }
  
  @Override
  public String getId() {
    return id;
  }

  @Override
  public Pipeline getPipeline() {
    return pipeline;
  }

  @Override
  public JobStatus getStatus() {
    return status;
  }
  
  @Override
  public void complete() {
    Assert.isTrue(status==JobStatus.STARTED,String.format("Job %s is %s and so can not be COMPLETED", id,status));
    status = JobStatus.COMPLETED;
  }

}
