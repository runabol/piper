/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.job;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import com.creactiviti.piper.core.task.PipelineTask;
import com.creactiviti.piper.core.task.SimplePipelineTask;
import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskExecution;
import com.creactiviti.piper.core.task.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDGenerator;
import com.creactiviti.piper.error.Error;
import com.creactiviti.piper.error.ErrorObject;


public class SimpleTaskExecution extends SimplePipelineTask implements TaskExecution {

  private SimpleTaskExecution () {
    this(Collections.EMPTY_MAP);
  }
  
  private SimpleTaskExecution (TaskExecution aSource) {
    this(aSource.asMap());
  }
  
  private SimpleTaskExecution (Map<String,Object> aSource) {
    super(aSource);
  }  
  
  private SimpleTaskExecution (Task aSource) {
    super(aSource);
  }
  
  @Override
  public String getId() {
    return getString("id");
  }
  
  public void setId (String aId) {
    set("id", aId);
  }
  
  @Override
  public String getParentId() {
    return getString("parentId");
  }
  
  public void setParentId (String aParentId) {
    set("parentId", aParentId);
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
  public Date getCreateTime() {
    return getDate("createTime");
  }
  
  public void setCreateTime(Date aDate) { 
    set("createTime", aDate);
  }
  
  @Override
  public Date getStartTime() {
    return getDate("startTime");
  }

  public void setStartTime(Date aDate) {
    set("startTime", aDate);
  }
  
  @Override
  public Date getEndTime() {
    return getDate("endTime");
  }
  
  public void setEndTime(Date aDate) {
    set("endTime", aDate);
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
  public int getRetryAttempts() {
    return getInteger("retryAttempts", 0);
  }
  
  @Override
  public String getRetryDelay() {
    return getString("retryDelay","1s");
  }
  
  @Override
  public long getRetryDelayMillis () {
    long delay = Duration.parse("PT" + getRetryDelay()).toMillis();
    int retryAttempts = getRetryAttempts();
    int retryDelayFactor = getRetryDelayFactor();
    return delay * retryAttempts * retryDelayFactor;
  }
  
  public void setRetryAttempts (int aRetryAttempts) {
    set("retryAttempts", aRetryAttempts);
  }
  
  @Override
  public int getRetryDelayFactor() {
    return getInteger("retryDelayFactor",2);
  }
    
  /**
   * Creates a new {@link SimpleTaskExecution} instance 
   * from a {@link PipelineTask}.
   * 
   * @param aTask
   *         The {@link PipelineTask} to create this instance from.
   * @return {@link SimpleTaskExecution}
   */
  public static SimpleTaskExecution createFrom (PipelineTask aTask) {
    SimpleTaskExecution jobTask = new SimpleTaskExecution (aTask);
    jobTask.setCreateTime(new Date());
    jobTask.setId(UUIDGenerator.generate());
    jobTask.setStatus(TaskStatus.CREATED);
    return jobTask;
  }
  
  /**
   * Creates a new {@link SimpleTaskExecution} instance, using the 
   * given {@link TaskExecution} instance as a starting point. 
   * 
   * @param aJobTask
   *          The {@link TaskExecution} instance to use as a starting 
   *          point.
   * @return the new {@link SimpleTaskExecution}
   */
  public static SimpleTaskExecution createNewFrom (TaskExecution aJobTask) {
    SimpleTaskExecution mutableJobTask = new SimpleTaskExecution(aJobTask);
    mutableJobTask.setId(UUIDGenerator.generate());
    mutableJobTask.setCreateTime(new Date());
    mutableJobTask.setStatus(TaskStatus.CREATED);
    mutableJobTask.setError(null);
    return mutableJobTask;
  }

  /**
   * Creates a {@link SimpleTaskExecution} instance which 
   * is a copy of a {@link TaskExecution}.
   * 
   * @param aJobTask
   *          The {@link TaskExecution} instance to copy.
   * @return the new {@link SimpleTaskExecution}
   */
  public static SimpleTaskExecution createForUpdate (TaskExecution aJobTask) {
    return new SimpleTaskExecution(aJobTask);
  }
  
  /**
   * Creates an empty {@link SimpleTaskExecution} instance.
   * 
   * @return The new {@link SimpleTaskExecution}.
   */
  public static SimpleTaskExecution create () {
    return new SimpleTaskExecution();
  }
  
  /**
   * Creates a {@link SimpleTaskExecution} instance for the given 
   * Key-Value pair.
   * 
   * @return The new {@link SimpleTaskExecution}.
   */
  public static SimpleTaskExecution createFrom (String aKey, Object aValue) {
    return new SimpleTaskExecution(Collections.singletonMap(aKey, aValue));
  }
  
  public static SimpleTaskExecution createFrom (String aKey1, Object aValue1, String aKey2, Object aValue2) {
    SimpleTaskExecution task = create();
    task.set(aKey1, aValue1);
    task.set(aKey2, aValue2);
    return task;
  }
 
  /**
   * Creates a {@link SimpleTaskExecution} instance for the given Key-Value
   * map.
   * 
   * @return The new {@link SimpleTaskExecution}.
   */
  public static SimpleTaskExecution createFromMap (Map<String,Object> aSource) {
    return new SimpleTaskExecution(aSource);
  }
  
}