package com.creactiviti.piper.core.job;

import java.util.Map;

import com.creactiviti.piper.core.JobTask;
import com.creactiviti.piper.core.Mutator;
import com.creactiviti.piper.core.SimpleTask;
import com.creactiviti.piper.core.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDFactory;


public class SimpleJobTask extends SimpleTask implements JobTask, Mutator {

  public SimpleJobTask (JobTask aSource) {
    super(aSource.toMap());
    set("id", aSource.getId());
  }
  
  public SimpleJobTask (Map<String, Object> aSource) {
    super(aSource);
    setIfNull("id", UUIDFactory.create());
    setIfNull("status", TaskStatus.CREATED);
  }
  
  @Override
  public String getId() {
    return getString("id");
  }

  @Override
  public String getStatus() {
    return getString("status");
  }
  
  @Override
  public Object getOutput() {
    return get("__output");
  }
  
  public void setOutput (Object aOutput) {
    set("__output", aOutput);
  }
  
  public void setStatus (String aStatus) {
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
  
}