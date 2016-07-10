package com.creactiviti.piper.core.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creactiviti.piper.core.task.JobTask;

public class SimpleJobRepository implements JobRepository {

  private final Map<String, MutableJob> jobs = new HashMap<>();
  
  @Override
  public MutableJob findOne (String aId) {
    return jobs.get(aId);
  }

  @Override
  public MutableJob save(MutableJob aJob) {
    return jobs.put(aJob.getId(), aJob);
  }
  
  @Override
  public MutableJob findJobByTaskId(String aTaskId) {
    
    for(MutableJob job : jobs.values()) {
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
