/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.job;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.creactiviti.piper.core.MapObject;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.error.Error;
import com.creactiviti.piper.error.ErrorObject;

public class MutableJob extends MapObject implements Job {

  public MutableJob () {
    super(Collections.EMPTY_MAP);
  }
  
  public MutableJob (Map<String,Object> aSource) {
    super(aSource);
  }
  
  public MutableJob (Job aSource) {
    super(aSource.asMap());
  }
    
  @Override
  public String getId() {
    return getString("id");
  }
  
  public void setId(String aId) {
    set("id", aId);
  }
  
  @Override
  public int getCurrentTask() {
    return getInteger("currentTask", -1);
  }
  
  public void setCurrentTask (int aCurrentStep) {
    set("currentTask", aCurrentStep);
  }
  
  @Override
  public String getName() {
    return getString("name");
  }
  
  public void setName(String aName) {
    set("name", aName);
  }
  
  @Override
  public Error getError() {
    if(containsKey("error")) {
      return new ErrorObject(getMap("error"));
    }
    return null;
  }
  
  public void setError (Error aError) {
    set("error", aError);
  }
  
  @Override
  public List<JobTask> getExecution() {
    List<JobTask> list = getList("execution", JobTask.class);
    return list!=null?list:Collections.EMPTY_LIST;
  }
  
  public void setExecution(List<JobTask> aExecution) {
    set("execution", aExecution);
  }
  
  @Override
  public JobStatus getStatus() {
    String value = getString("status");
    return value!=null?JobStatus.valueOf(value):null;
  }
  
  public void setStatus (JobStatus aStatus) {
    set("status", aStatus);
  }
  
  public void setCompletionDate(Date aCompletionDate) {
    set("completionDate", aCompletionDate);
  }
  
  public void setStartDate(Date aStartDate) {
    set("startDate",aStartDate);
  }
  
  public void setFailedDate(Date aFailedDate) {
    set("failedDate",aFailedDate);
  }
  
  @Override
  public Date getCreationDate() {
    return getDate("creationDate");
  }
  
  public void setCreationDate(Date aCreationDate) {
    set("creationDate",aCreationDate);
  }
  
  @Override
  public String getPipelineId() {
    return getString("pipelineId");
  }
  
  public void setPipelineId(String aPipelineId) {
    set("pipelineId",aPipelineId);
  }
  
  @Override
  public Date getStartDate() {
    return getDate("startDate");
  }
  
  @Override
  public Date getFailedDate() {
    return getDate("failedDate");
  }
  
  @Override
  public Date getCompletionDate() {
    return getDate("completionDate");
  }
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
  
}
