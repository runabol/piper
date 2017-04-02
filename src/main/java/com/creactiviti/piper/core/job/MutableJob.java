/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.uuid.UUIDGenerator;

public class MutableJob implements Job {

  private final String id;
  private final String pipelineId;
  private String name;
  private final Date creationDate;
  
  private JobStatus status = JobStatus.CREATED;
  private Map<String,JobTask> execution = new LinkedHashMap<>();
  
  private Date completionDate;
  private Date startDate;
  private Date failedDate;
  
  /**
   * Constructs a new {@link Job} instance.
   * 
   * @param aJob
   */
  public MutableJob (String aPipelineId) {
    creationDate = new Date();
    id = UUIDGenerator.generate();
    pipelineId = aPipelineId;
  }
  
  /**
   * Constructs a mutable version of a {@link Job}
   * instance.
   * 
   * @param aSource
   */
  public MutableJob (Job aSource) {
    id = aSource.getId();
    pipelineId = aSource.getPipelineId();
    creationDate = aSource.getCreationDate();
    status = aSource.getStatus();
    aSource.getExecution().forEach(t->execution.put(t.getId(), t));
    completionDate = aSource.getCompletionDate();
    startDate = aSource.getStartDate();
    name = aSource.getName();
  }
    
  @Override
  public String getId() {
    return id;
  }
  
  @Override
  public String getName() {
    return name!=null?name:getPipelineId();
  }
  
  public void setName(String aName) {
    name = aName;
  }
  
  @Override
  public List<JobTask> getExecution() {
    return Collections.unmodifiableList(new ArrayList<JobTask>(execution.values()));
  }
  
  public void addTask (JobTask aTask) {
    execution.put(aTask.getId(), aTask);
  }
    
  @Override
  public JobStatus getStatus() {
    return status;
  }
  
  public void setStatus (JobStatus aStatus) {
    if(aStatus == JobStatus.COMPLETED) {
      Assert.isTrue(status==JobStatus.STARTED,String.format("Job %s is %s and so can not be COMPLETED", id,status));
      status = JobStatus.COMPLETED;
      completionDate = new Date();
    }
    else if (aStatus == JobStatus.STARTED) {
      Assert.isTrue(status==JobStatus.CREATED||status==JobStatus.FAILED||status==JobStatus.STOPPED,String.format("Job %s is %s and so can not be STARTED", id,status));
      status = JobStatus.STARTED;
      startDate = new Date();
    }
    else if (aStatus == JobStatus.FAILED) {
      status = JobStatus.FAILED;
      failedDate = new Date();
    }
    else {
      throw new IllegalArgumentException("Can't handle status: " + aStatus);
    }
  }

  public void updateTask (JobTask aJobTask) {
    Assert.isTrue(execution.containsKey(aJobTask.getId()),"Unkown task: " + aJobTask.getId());
    execution.put(aJobTask.getId(), aJobTask);
  }
  
  @Override
  public Date getCreationDate() {
    return creationDate;
  }
  
  @Override
  public String getPipelineId() {
    return pipelineId;
  }
  
  @Override
  public Date getStartDate() {
    return startDate;
  }
  
  @Override
  public Date getFailedDate() {
    return failedDate;
  }
  
  @Override
  public Date getCompletionDate() {
    return completionDate;
  }
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
  
}
