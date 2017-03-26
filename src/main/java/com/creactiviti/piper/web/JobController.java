package com.creactiviti.piper.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobRepository;

@RestController
@RequestMapping("/job")
public class JobController {

  @Autowired
  private JobRepository jobRepository;
  
  @Autowired
  private Coordinator coordinator;
  
  private static final String PIPELINE = "pipeline";
  private static final String INPUT = "input";
  
  @PostMapping(value="/start")
  public Job start (@RequestBody Map<String, Object> aJobRequest) {
    String pipelineId = (String) aJobRequest.get(PIPELINE);
    Assert.notNull(pipelineId,"Missing required field: " + PIPELINE);
    Map<String, Object> input = (Map<String, Object>) aJobRequest.get(INPUT);
    return coordinator.start(pipelineId, input);
  }
  
  @GetMapping(value="/{id}")
  public Job get (@PathVariable("id")String aJobId) {
    return jobRepository.findOne (aJobId);
  }
  
}
