package com.creactiviti.piper.core.job;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.JobStatus;
import com.creactiviti.piper.core.JobTask;

@Component
public class SimpleJobRepository implements JobRepository<SimpleJob> {

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
