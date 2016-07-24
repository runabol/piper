package com.creactiviti.piper.core;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.context.Context;
import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.context.SimpleContext;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.job.JobStatus;
import com.creactiviti.piper.core.job.SimpleJob;
import com.creactiviti.piper.core.job.SimpleJobTask;
import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.pipeline.PipelineRepository;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.TaskStatus;

@Component
public class DefaultCoordinator implements Coordinator {

  private Messenger messenger;
  private PipelineRepository pipelineRepository;
  private JobRepository jobRepository;
  private ApplicationEventPublisher eventPublisher;
  private ContextRepository contextRepository;
  
  private static final String DEFAULT_TASK_QUEUE = "tasks";
 
  private final Logger log = LoggerFactory.getLogger(getClass());
  
  @Override
  public Job start (String aPipelineId, Map<String, Object> aParameters) {
    Assert.notNull(aPipelineId,"pipelineId must not be null");
    
    Pipeline pipeline = pipelineRepository.findOne(aPipelineId);
    Assert.notNull(pipeline,String.format("Unkown pipeline: %s", aPipelineId));
    
    SimpleJob job = new SimpleJob(pipeline);
    job.setStatus(JobStatus.STARTED);
    log.debug("Job {} started",job.getId());
    jobRepository.save(job);
    
    Context context = new SimpleContext(job.getId(), aParameters);
    contextRepository.save(context);
    
    run(job);
    
    return job;
  }
  
  private void run (Job aJob) {
    if(aJob.hasMoreTasks()) {
      JobTask nextTask = jobRepository.nextTask(aJob);
      String node = nextTask.getNode();
      messenger.send(node!=null?node:DEFAULT_TASK_QUEUE, nextTask);
    }
    else {
      SimpleJob job = new SimpleJob(aJob);
      job.setStatus(JobStatus.COMPLETED);
      jobRepository.save(job);
      log.debug("Job {} completed successfully",aJob.getId());
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
  public void complete (JobTask aTask) {
    log.debug("Completing task {}", aTask.getId());
    SimpleJobTask task = new SimpleJobTask(aTask);
    task.setStatus(TaskStatus.COMPLETED);
    SimpleJob job = new SimpleJob (jobRepository.findJobByTaskId (aTask.getId()));
    Assert.notNull(job,String.format("No job found for task %s ",aTask.getId()));
    job.updateTask(task);
    jobRepository.save(job);
    run(job);
  }

  @Override
  public void error (JobTask aTask) {
  }

  @Override
  public void on (Object aEvent) {
    eventPublisher.publishEvent (aEvent);    
  }
  
  @Autowired
  public void setContextRepository(ContextRepository aContextRepository) {
    contextRepository = aContextRepository;
  }
  
  @Autowired
  public void setEventPublisher(ApplicationEventPublisher aEventPublisher) {
    eventPublisher = aEventPublisher;
  }
  
  @Autowired
  public void setJobRepository(JobRepository aJobRepository) {
    jobRepository = aJobRepository;
  }
  
  @Autowired
  public void setMessenger(Messenger aMessenger) {
    messenger = aMessenger;
  }
  
  @Autowired
  public void setPipelineRepository(PipelineRepository aPipelineRepository) {
    pipelineRepository = aPipelineRepository;
  }

}
