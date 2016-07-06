package com.creactiviti.piper.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SimpleJobRepository implements JobRepository<SimpleJob> {

  private final Map<String, SimpleJob> jobs = new HashMap<>();
  
  @Override
  public List<SimpleJob> findAll() {
    return new ArrayList<SimpleJob>(jobs.values());
  }

  @Override
  public SimpleJob findOne (String aId) {
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
  public Task nextTask(SimpleJob aJob) {
    return aJob.nextTask();
  }

}
