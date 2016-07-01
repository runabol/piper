package com.creactiviti.piper.core;

import java.util.UUID;

import org.springframework.util.Assert;

public class MutableJob implements Job {

  private final String id = UUID.randomUUID().toString();
  private final Pipeline pipeline;
  private JobStatus status = JobStatus.CREATED;
  
  public MutableJob (Pipeline aPipeline) {
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
