package com.creactiviti.piper.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
  
  private static final String PIPELINE_ID = "pipelineId";
  
  @RequestMapping(value="/start",method=RequestMethod.POST)
  public Job start (@RequestBody Map<String, Object> aJobRequest) {
    String pipelineId = (String) aJobRequest.remove(PIPELINE_ID);
    Assert.notNull(pipelineId,"Missing required field: " + PIPELINE_ID);
    return coordinator.start(pipelineId, aJobRequest);
  }
  
  @RequestMapping(value="/{id}",method=RequestMethod.GET)
  public Job start (@PathVariable("id")String aJobId) {
    return jobRepository.findOne (aJobId);
  }
  
  public static void main (String[] args) {
    SimpleDateFormat x = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
    System.out.println(x.format(new Date()));
  }
  
}
