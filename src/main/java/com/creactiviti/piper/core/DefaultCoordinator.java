package com.creactiviti.piper.core;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DefaultCoordinator implements Coordinator {

  @Autowired private Messenger messenger;
  @Autowired private PipelineFactory pipelineFactory;
  
  private static final String PIPELINE_ID = "pipelineId";
  
  @Override
  public Job start(Map<String, Object> aInput) {
    String pipelineId = (String) aInput.get(PIPELINE_ID);
    Assert.notNull(pipelineId,String.format("Missing mandatory parameter %s", PIPELINE_ID));
    Pipeline pipeline = pipelineFactory.getPipeline(pipelineId);
    Assert.notNull(pipeline,String.format("Unkown pipeline: %s", pipelineId));
    return new SimpleJob(pipeline);
  }

  @Override
  public Job stop (String aJobId) {
    return null;
  }

  @Override
  public Job resume (String aJobId) {
    return null;
  }

  @Override
  public Job get (String aJobId) {
    return null;
  }

}
