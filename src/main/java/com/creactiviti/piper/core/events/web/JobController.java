package com.creactiviti.piper.core.events.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.Job;

@RestController
@RequestMapping("/job")
public class JobController {

  @Autowired
  private Coordinator coordinator;
  
  @RequestMapping(value="/start",method=RequestMethod.POST)
  public Job start (@RequestBody Map<String, Object> aJobRequest) {
    return coordinator.start(aJobRequest);
  }
  
}
