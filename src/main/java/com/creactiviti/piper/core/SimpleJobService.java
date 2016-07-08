package com.creactiviti.piper.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SimpleJobService implements JobService<SimpleJob> {

  private final Map<String, SimpleJob> jobs = new HashMap<>();
  private final Map<String, SimpleJob> taskToJob = new HashMap<>();
  
  @Override
  public SimpleJob getJobById (String aId) {
    return jobs.get(aId);
  }

  @Override
  public SimpleJob save(SimpleJob aJob) {
    return jobs.put(aJob.getId(), aJob);
  }
  
  @Override
  public SimpleJob updateStatus (SimpleJob aJob, JobStatus aJobStatus) {
    aJob.setStatus(aJobStatus);
    return save(aJob);
  }
  
  @Override
  public JobTask nextTask(SimpleJob aJob) {
    JobTask nextTask = aJob.nextTask();
    taskToJob.put(nextTask.getId(), aJob);
    return nextTask;
  }

  @Override
  public SimpleJob updateTask(SimpleJob aJob, JobTask aTask) {
    aJob.updateTask(aTask);
    return aJob;
  }
  
  @Override
  public SimpleJob getJobByTaskId(String aTaskId) {
    return taskToJob.get(aTaskId);
  }

}
