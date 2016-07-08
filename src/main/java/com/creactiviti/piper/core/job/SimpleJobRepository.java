package com.creactiviti.piper.core.job;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.JobTask;

@Component
public class SimpleJobRepository implements MutableJobRepository<SimpleJob> {

  private final Map<String, SimpleJob> jobs = new HashMap<>();
  private final Map<String, SimpleJob> taskToJob = new HashMap<>();
  
  @Override
  public SimpleJob findOne (String aId) {
    return jobs.get(aId);
  }

  @Override
  public SimpleJob save(SimpleJob aJob) {
    return jobs.put(aJob.getId(), aJob);
  }
  
  @Override
  public JobTask nextTask(SimpleJob aJob) {
    JobTask nextTask = aJob.nextTask();
    taskToJob.put(nextTask.getId(), aJob);
    return nextTask;
  }

  @Override
  public SimpleJob findJobByTaskId(String aTaskId) {
    return taskToJob.get(aTaskId);
  }

}
