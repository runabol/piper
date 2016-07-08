package com.creactiviti.piper.core.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.JobTask;

@Component
public class SimpleJobRepository implements MutableJobRepository<SimpleJob> {

  private final Map<String, SimpleJob> jobs = new HashMap<>();
  
  @Override
  public SimpleJob findOne (String aId) {
    return jobs.get(aId);
  }

  @Override
  public SimpleJob save(SimpleJob aJob) {
    return jobs.put(aJob.getId(), aJob);
  }
  
  @Override
  public SimpleJob findJobByTaskId(String aTaskId) {
    
    for(SimpleJob job : jobs.values()) {
      List<JobTask> tasks = job.getTasks();
      for(JobTask t : tasks) {
        if(t.getId().equals(aTaskId)) {
          return job;
        }
      }
    }
    
    return null;
  }

}
