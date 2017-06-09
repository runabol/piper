package com.creactiviti.piper.webhook;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.web.client.RestTemplate;

import com.creactiviti.piper.core.Accessor;
import com.creactiviti.piper.core.DSL;
import com.creactiviti.piper.core.event.Events;
import com.creactiviti.piper.core.event.PiperEvent;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobRepository;

public class WebhookEventHandler implements ApplicationListener<PiperEvent>{
  
  private final JobRepository jobRepository;
  
  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  private final RestTemplate rest = new RestTemplate();
  
  public WebhookEventHandler(JobRepository aJobRepository) {
    jobRepository = aJobRepository;
  }
  
  public void handleEvent (PiperEvent aEvent) {
    String jobId = aEvent.getRequiredString(DSL.JOB_ID);
    Job job = jobRepository.findOne(jobId);
    if(job == null) {
      logger.warn("Unknown job: {}", jobId);
      return;
    }
    List<Accessor> webhooks = job.getWebhooks();
    for(Accessor webhook : webhooks) {
      if(Events.JOB_STATUS.equals(webhook.getRequiredString(DSL.TYPE))) {
        rest.postForObject(webhook.getRequiredString(DSL.URL), aEvent, String.class);
      }
    }
  }
  
  @Override
  public void onApplicationEvent (PiperEvent aEvent) {
    if(aEvent.getType().equals(Events.JOB_STATUS)) {
      handleEvent(aEvent);
    }
  }
  
}
