package com.creactiviti.piper;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("piper.coordinator")
public class CoordinatorProperties {

  private QueueProperties completions = new QueueProperties ();
  private QueueProperties errors = new QueueProperties ();
  private QueueProperties events = new QueueProperties ();
  
  public QueueProperties getCompletions() {
    return completions;
  }
  
  public QueueProperties getErrors() {
    return errors;
  }
  
  public QueueProperties getEvents() {
    return events;
  }
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
    
}
