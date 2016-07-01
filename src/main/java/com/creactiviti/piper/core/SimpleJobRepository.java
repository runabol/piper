package com.creactiviti.piper.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SimpleJobRepository implements JobRepository {

  private final Map<String, Job> jobs = new HashMap<>();
  
  @Override
  public List<Job> findAll() {
    return new ArrayList<Job>(jobs.values());
  }

  @Override
  public Job find(String aId) {
    return jobs.get(aId);
  }

  @Override
  public Job save(Job aJob) {
    return jobs.put(aJob.getId(), aJob);
  }

}
