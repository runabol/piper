/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.MapObject;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobRepository;

@RestController
public class JobController {

  @Autowired
  private JobRepository jobRepository;
  
  @Autowired
  private Coordinator coordinator;
  
  @PostMapping("/jobs")
  public Job create (@RequestBody Map<String, Object> aJobRequest) {
    return coordinator.start(MapObject.of(aJobRequest));
  }
  
  @GetMapping(value="/jobs/{id}")
  public Job get (@PathVariable("id")String aJobId) {
    return jobRepository.findOne (aJobId);
  }
  
}
