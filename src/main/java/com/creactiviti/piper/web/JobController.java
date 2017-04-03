/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.MapObject;
import com.creactiviti.piper.core.Page;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.job.MutableJob;

@RestController
public class JobController {

  @Autowired
  private JobRepository jobRepository;
  
  @Autowired
  private Coordinator coordinator;
  
  /**
   * List all jobs
   * 
   * @return
   */
  @GetMapping(value="/jobs")
  public Page<Job> list (@RequestParam(value="p",defaultValue="1") Integer aPageNumber) {
    return jobRepository.findAll(aPageNumber);
  }
  
  @PostMapping("/jobs")
  public Job create (@RequestBody Map<String, Object> aJobRequest) {
    return coordinator.start(MapObject.of(aJobRequest));
  }
  
  @GetMapping(value="/jobs/{id}")
  public Job get (@PathVariable("id")String aJobId) {
    Job job = jobRepository.findOne (aJobId);
    Assert.notNull(job,"Unknown job: " + aJobId);
    MutableJob mjob = new MutableJob(job);
    mjob.setExecution(jobRepository.getExecution(job.getId()));
    return mjob;
  }
    
}
