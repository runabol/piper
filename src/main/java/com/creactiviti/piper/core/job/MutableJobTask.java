/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.job;

import java.util.Date;
import java.util.Map;

import com.creactiviti.piper.core.Mutator;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.MutableTask;
import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDGenerator;


public class MutableJobTask extends MutableTask implements JobTask, Mutator {

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
  public Exception getException() {
    return (Exception) get("exception");
  }
  
  public void setException (Throwable aException) {
    set("exception", aException);
  }
  
  public void setStatus (TaskStatus aStatus) {
    set("status",aStatus);
  }
  
  @Override
  public void set (String aKey, Object aValue) {
    put(aKey, aValue);
  }
  
  @Override
  public void setIfNull(String aKey, Object aValue) {
    if(get(aKey)==null) {
      set(aKey, aValue);
    }
  }

  @Override
  public Date getCreationDate() {
    return getDate("creationDate");
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
  
}