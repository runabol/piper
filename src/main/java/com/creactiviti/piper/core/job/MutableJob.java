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
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.task.JobTask;

public class MutableJob implements Job {

  private String id;
  private String pipelineId;
  private String name;
  private Date creationDate;
  
  private JobStatus status = JobStatus.CREATED;
  private List<JobTask> execution = new ArrayList<>();
  
  private Date completionDate;
  private Date startDate;
  private Date failedDate;
  
  public MutableJob () {}
  
  /**
   * Constructs a mutable version of a {@link Job}
   * instance.
   * 
   * @param aSource
   */
  public MutableJob (Job aSource) {
    BeanUtils.copyProperties(aSource, this);
  }
    
  @Override
  public String getId() {
    return id;
  }
  
  public void setId(String aId) {
    id = aId;
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
    return Collections.unmodifiableList(execution);
  }
  
  public void setExecution(List<JobTask> aExecution) {
    Assert.notNull(aExecution, "execution list can't be null");
    execution = new ArrayList<>(aExecution);
  }
  
  public void addTask (JobTask aTask) {
    execution.add(aTask);
  }
    
  @Override
  public JobStatus getStatus() {
    return status;
  }
  
  public void setStatus (JobStatus aStatus) {
    status = aStatus;
  }
  
  public void setCompletionDate(Date aCompletionDate) {
    completionDate = aCompletionDate;
  }
  
  public void setStartDate(Date aStartDate) {
    startDate = aStartDate;
  }
  
  public void setFailedDate(Date aFailedDate) {
    failedDate = aFailedDate;
  }
  
  public void updateTask (JobTask aJobTask) {
    JobTask existingTask = findTask(aJobTask.getId());
    Assert.isTrue(existingTask!=null,"Unknown task: " + aJobTask.getId());
    execution.set(execution.indexOf(existingTask), aJobTask);
  }
  
  private JobTask findTask (String aTaskId) {
    for(JobTask t : execution) {
      if(t.getId().equals(aTaskId)) {
        return t;
      }
    }
    return null;
  }
  
  @Override
  public Date getCreationDate() {
    return creationDate;
  }
  
  @Override
  public String getPipelineId() {
    return pipelineId;
  }
  
  public void setPipelineId(String aPipelineId) {
    pipelineId = aPipelineId;
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
