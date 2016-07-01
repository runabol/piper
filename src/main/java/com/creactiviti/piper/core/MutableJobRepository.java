package com.creactiviti.piper.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class MutableJobRepository implements JobRepository {

  private final Map<String, MutableJob> jobs = new HashMap<>();
  
  @Override
  public List<MutableJob> findAll() {
    return new ArrayList<MutableJob>(jobs.values());
  }

  @Override
  public MutableJob find(String aId) {
    return jobs.get(aId);
  }

  @Override
  public MutableJob save(MutableJob aJob) {
    return jobs.put(aJob.getId(), aJob);
  }

}
