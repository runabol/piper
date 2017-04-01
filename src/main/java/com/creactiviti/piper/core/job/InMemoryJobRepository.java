/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.creactiviti.piper.core.task.JobTask;

public class InMemoryJobRepository implements JobRepository {

  private final Map<String,Job> jobs = Collections.synchronizedMap(new LinkedHashMap<>());
  
  @Override
  public Job findOne (String aId) {
    return jobs.get(aId);
  }

  @Override
  public Job save(Job aJob) {
    return jobs.put(aJob.getId(), aJob);
  }
  
  @Override
  public Job findJobByTaskId(String aTaskId) {
    
    for(Job job : jobs.values()) {
      List<JobTask> tasks = job.getExecution();
      for(JobTask t : tasks) {
        if(t.getId().equals(aTaskId)) {
          return job;
        }
      }
    }
    
    return null;
  }

  @Override
  public List<Job> findAll() {
    ArrayList<Job> list = new ArrayList<>(jobs.values());
    Collections.reverse(list);
    return list;
  }

}
