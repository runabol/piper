package com.creactiviti.piper.core;

import java.util.UUID;

import org.springframework.util.Assert;

public class SimpleJob implements Job {

  private final String id = UUID.randomUUID().toString();
  private final Pipeline pipeline;
  
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

}
