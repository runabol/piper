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

import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.MutablePipelineTask;
import com.creactiviti.piper.core.task.PipelineTask;
import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDGenerator;
import com.creactiviti.piper.error.Error;
import com.creactiviti.piper.error.ErrorObject;


public class MutableJobTask extends MutablePipelineTask implements JobTask {

  private MutableJobTask () {
    this(Collections.EMPTY_MAP);
  }
  
  private MutableJobTask (JobTask aSource) {
    this(aSource.asMap());
  }
  
  private MutableJobTask (Map<String,Object> aSource) {
    super(aSource);
  }  
  
  private MutableJobTask (Task aSource) {
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
  public Date getCancellationDate() {
    return getDate("cancellationDate");
  }
  
  public void setCancellationDate (Date aDate) {
    set("cancellationDate", aDate);
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
  
  @Override
  public String getTimeout() {
    return getString("timeout");
  }
  
  /**
   * Creates a new {@link MutableJobTask} instance 
   * from a {@link PipelineTask}.
   * 
   * @param aTask
   *         The {@link PipelineTask} to create this instance from.
   * @return {@link MutableJobTask}
   */
  public static MutableJobTask createFrom (PipelineTask aTask) {
    MutableJobTask jobTask = new MutableJobTask (aTask);
    jobTask.setCreationDate(new Date());
    jobTask.setId(UUIDGenerator.generate());
    jobTask.setStatus(TaskStatus.CREATED);
    return jobTask;
  }
  
  /**
   * Creates a new {@link MutableJobTask} instance, using the 
   * given {@link JobTask} instance as a starting point. 
   * 
   * @param aJobTask
   *          The {@link JobTask} instance to use as a starting 
   *          point.
   * @return the new {@link MutableJobTask}
   */
  public static MutableJobTask createNewFrom (JobTask aJobTask) {
    MutableJobTask mutableJobTask = new MutableJobTask(aJobTask);
    mutableJobTask.setId(UUIDGenerator.generate());
    mutableJobTask.setCreationDate(new Date());
    mutableJobTask.setStatus(TaskStatus.CREATED);
    mutableJobTask.setError(null);
    return mutableJobTask;
  }

  /**
   * Creates a {@link MutableJobTask} instance which 
   * is a copy of a {@link JobTask}.
   * 
   * @param aJobTask
   *          The {@link JobTask} instance to copy.
   * @return the new {@link MutableJobTask}
   */
  public static MutableJobTask createForUpdate (JobTask aJobTask) {
    return new MutableJobTask(aJobTask);
  }
  
  /**
   * Creates an empty {@link MutableJobTask} instance.
   * 
   * @return The new {@link MutableJobTask}.
   */
  public static MutableJobTask create () {
    return new MutableJobTask();
  }
  
  /**
   * Creates a {@link MutableJobTask} instance for the given 
   * Key-Value pair.
   * 
   * @return The new {@link MutableJobTask}.
   */
  public static MutableJobTask createFrom (String aKey, Object aValue) {
    return new MutableJobTask(Collections.singletonMap(aKey, aValue));
  }
 
  /**
   * Creates a {@link MutableJobTask} instance for the given Key-Value
   * map.
   * 
   * @return The new {@link MutableJobTask}.
   */
  public static MutableJobTask createFromMap (Map<String,Object> aSource) {
    return new MutableJobTask(aSource);
  }
  
}