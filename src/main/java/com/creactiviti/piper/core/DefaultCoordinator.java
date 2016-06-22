package com.creactiviti.piper.core;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DefaultCoordinator implements Coordinator {

  @Autowired private Messenger messenger;
  @Autowired private PipelineFactory pipelineFactory;
  @Autowired private JobRepository jobRepository;
  @Autowired private ApplicationEventPublisher eventPublisher;
  
  private static final String PIPELINE_ID = "pipelineId";
  
  @Override
  public Job start (Map<String, Object> aInput) {
    String pipelineId = (String) aInput.get(PIPELINE_ID);
    Assert.notNull(pipelineId,String.format("Missing mandatory parameter %s", PIPELINE_ID));
    Pipeline pipeline = pipelineFactory.createPipeline(pipelineId);
    Assert.notNull(pipeline,String.format("Unkown pipeline: %s", pipelineId));
    SimpleJob simpleJob = new SimpleJob(pipeline);
    jobRepository.save(simpleJob);
    run(simpleJob);
    return simpleJob;
  }
  
  private void run (Job aJob) {
    Pipeline pipeline = aJob.getPipeline();
    if(pipeline.hasNextTask()) {
      Task nextTask = pipeline.nextTask();
      messenger.send(nextTask.getNode(), nextTask);
    }
    else {
      aJob.complete();
      jobRepository.save(aJob);
    }
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

  @Override
  public void complete (Task aTask) {
  }

  @Override
  public void error (Task aTask) {
  }

  @Override
  public void on (Object aEvent) {
    eventPublisher.publishEvent (aEvent);    
  }

}
