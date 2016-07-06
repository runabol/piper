package com.creactiviti.piper.core.events.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.Job;
import com.creactiviti.piper.core.JobRepository;

@RestController
@RequestMapping("/job")
public class JobController {

  @Autowired
  private JobRepository jobRepository;
  
  @Autowired
  private Coordinator coordinator;
  
  @RequestMapping(value="/start",method=RequestMethod.POST)
  public Job start (@RequestBody Map<String, Object> aJobRequest) {
    return coordinator.start(aJobRequest);
  }
  
  @RequestMapping(value="/{id}",method=RequestMethod.GET)
  public Job start (@PathVariable("id")String aJobId) {
    return jobRepository.findOne (aJobId);
  }
  
}
