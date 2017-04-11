/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.job;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.MutableTask;
import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDGenerator;
import com.creactiviti.piper.error.Error;
import com.creactiviti.piper.error.ErrorObject;


public class MutableJobTask extends MutableTask implements JobTask {

  public MutableJobTask () {
    this(Collections.EMPTY_MAP);
  }
  
  public MutableJobTask (JobTask aSource) {
    this(aSource.asMap());
  }
  
  public MutableJobTask (Map<String,Object> aSource) {
    super(aSource);
  }  
  
  public MutableJobTask (Task aSource) {
    super(aSource);
    set("id", UUIDGenerator.generate());
    set("status", TaskStatus.CREATED);
    set("creationDate", new Date());
  }
  
  @Override
  public String getId() {
    return getString("id");
  }
  
  public void setId (String aId) {
    set("id", aId);
  }
  
  @Override
  public String getJobId() {
    return getString("jobId");
  }

  public void setJobId (String aJobId) {
    set("jobId", aJobId);
  }

  @Override
  public TaskStatus getStatus() {
    String status = getString("status");
    if(status == null) return null;
    return TaskStatus.valueOf(status);
  }
  
  @Override
  public Object getOutput() {
    return get("output");
  }
  
  public void setOutput (Object aOutput) {
    set("output", aOutput);
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
  
  public void setStatus (TaskStatus aStatus) {
    set("status",aStatus);
  }
  
  @Override
  public void set (String aKey, Object aValue) {
    put(aKey, aValue);
  }
  
  @Override
  public Date getCreationDate() {
    return getDate("creationDate");
  }
  
  public void setCreationDate(Date aDate) { 
    set("creationDate", aDate);
  }

  @Override
  public Date getCompletionDate() {
    return getDate("completionDate");
  }
  
  public void setCompletionDate(Date aDate) {
    set("completionDate", aDate);
  }

  @Override
  public Date getFailedDate() {
    return getDate("failedDate");
  }

  @Override
  public long getExecutionTime() {
    return 0;
  }
  
  @Override
  public int getRetry() {
    return getInteger("retry", 0);
  }
  
  @Override
  public long getRetryDelay() {
    return getInteger("retryDelay",0);
  }
  
  public void setRetryDelay (long aDelay) {
    set("retryDelay", aDelay);
  }
  
  public void setRetry (int aValue) {
    set("retry", aValue);
  }
  
  
  
}