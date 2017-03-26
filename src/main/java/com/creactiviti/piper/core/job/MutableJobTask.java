package com.creactiviti.piper.core.job;

import java.util.Date;
import java.util.Map;

import com.creactiviti.piper.core.Mutator;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.MutableTask;
import com.creactiviti.piper.core.task.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDFactory;


public class MutableJobTask extends MutableTask implements JobTask, Mutator {

  public MutableJobTask (JobTask aSource) {
    super(aSource.toMap());
  }
  
  public MutableJobTask (Map<String, Object> aSource) {
    super(aSource);
    setIfNull("id", UUIDFactory.create());
    setIfNull("status", TaskStatus.CREATED);
    setIfNull("creationDate", new Date());
  }
  
  @Override
  public String getId() {
    return getString("id");
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
  
  public void setException (Exception aException) {
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