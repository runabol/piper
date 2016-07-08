package com.creactiviti.piper.core.job;

import java.util.Date;
import java.util.Map;

import com.creactiviti.piper.core.Mutator;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.SimpleTask;
import com.creactiviti.piper.core.task.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDFactory;


public class SimpleJobTask extends SimpleTask implements JobTask, Mutator {

  public SimpleJobTask (JobTask aSource) {
    super(aSource.toMap());
  }
  
  public SimpleJobTask (Map<String, Object> aSource) {
    super(aSource);
    setIfNull("_id", UUIDFactory.create());
    setIfNull("_status", TaskStatus.CREATED);
    setIfNull("_creationDate", new Date());
  }
  
  @Override
  public String getId() {
    return getString("_id");
  }

  @Override
  public TaskStatus getStatus() {
    String status = getString("_status");
    if(status == null) return null;
    return TaskStatus.valueOf(status);
  }
  
  @Override
  public Object getOutput() {
    return get("_output");
  }
  
  public void setOutput (Object aOutput) {
    set("_output", aOutput);
  }
  
  public void setStatus (TaskStatus aStatus) {
    set("_status",aStatus);
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
    return getDate("_creationDate");
  }

  @Override
  public Date getCompletionDate() {
    return getDate("_completionDate");
  }
  
  public void setCompletionDate(Date aDate) {
    set("_completionDate", aDate);
  }

  @Override
  public Date getFailedDate() {
    return getDate("_failedDate");
  }
  
}