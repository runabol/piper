package com.creactiviti.piper.core;

public class Job {

  private String id;
  private String pipelineId;
  private JobStatus status;

  public String getId() {
    return id;
  }
  
  public void setId(String aId) {
    id = aId;
  }
  
  public String getPipelineId() {
    return pipelineId;
  }
  
  public void setPipelineId(String aPipelineId) {
    pipelineId = aPipelineId;
  }
  
  public JobStatus getStatus() {
    return status;
  }
  
  public void setStatus(JobStatus aStatus) {
    status = aStatus;
  }
  
}
