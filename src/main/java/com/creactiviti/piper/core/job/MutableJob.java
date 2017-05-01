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

import org.springframework.beans.BeanUtils;

import com.creactiviti.piper.core.MapObject;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.error.Error;
import com.creactiviti.piper.error.ErrorObject;

/**
 * An implementation of the {@link Job} interface
 * based on {@link MapObject}.
 * 
 * @author Arik Cohen
 */
public class MutableJob extends MapObject implements Job {

  public MutableJob () {
    super(Collections.EMPTY_MAP);
  }
  
  public MutableJob (Map<String,Object> aSource) {
    super(aSource);
  }
  
  public MutableJob (Job aSource) {
    super();
    BeanUtils.copyProperties(aSource, this);
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
    if(get("error")!=null) {
      return new ErrorObject(getMap("error"));
    }
    return null;
  }
  
  public void setError (Error aError) {
    set("error", aError);
  }
  
  @Override
  public List<JobTask> getTasks() {
    List<JobTask> list = getList("tasks", JobTask.class);
    return list!=null?list:Collections.EMPTY_LIST;
  }
  
  public void setTasks(List<JobTask> aTasks) {
    set("tasks", aTasks);
  }
  
  @Override
  public JobStatus getStatus() {
    String value = getString("status");
    return value!=null?JobStatus.valueOf(value):null;
  }
  
  public void setStatus (JobStatus aStatus) {
    set("status", aStatus);
  }
  
  public void setEndTime(Date aEndTime) {
    set("endTime", aEndTime);
  }
  
  public void setStartTime(Date aStartTime) {
    set("startTime",aStartTime);
  }
  
  @Override
  public Date getCreateTime() {
    return getDate("createTime");
  }
  
  public void setCreateTime (Date aCreateTime) {
    set("createTime",aCreateTime);
  }
  
  @Override
  public String getPipelineId() {
    return getString("pipelineId");
  }
  
  public void setPipelineId(String aPipelineId) {
    set("pipelineId",aPipelineId);
  }
  
  @Override
  public Date getStartTime() {
    return getDate("startTime");
  }
  
  @Override
  public Date getEndTime() {
    return getDate("endTime");
  }
  
}
