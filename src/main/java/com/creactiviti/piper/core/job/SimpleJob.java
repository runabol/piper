package com.creactiviti.piper.core.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.creactiviti.piper.core.Task;
import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.uuid.UUIDFactory;

public class SimpleJob implements MutableJob {

  private final String id = UUIDFactory.create();
  private final Pipeline pipeline;
  private JobStatus status = JobStatus.CREATED;
  private final Map<String,JobTask> tasks = new LinkedHashMap<>();
  private int nextTask = 0;
  private final Date dateCreated = new Date();
  private Date dateCompleted;
  private Date dateStarted;
  
  public SimpleJob (Pipeline aPipeline) {
    Assert.notNull(aPipeline,"pipeline must not be null");
    pipeline = aPipeline;
  }
  
  @Override
  public String getId() {
    return id;
  }

  @Override
  public List<JobTask> getTasks() {
    return Collections.unmodifiableList(new ArrayList<JobTask>(tasks.values()));
  }
  
  @Override
  public boolean hasMoreTasks() {
    return nextTask < pipeline.getTasks().size();
  }
  
  public JobTask nextTask() {
    Task task = pipeline.getTasks().get(nextTask);
    nextTask++;
    SimpleJobTask mt = new SimpleJobTask (task.toMap());
    tasks.put(mt.getId(),mt);
    return mt;
  }
  
  @Override
  public void updateTask (JobTask aTask) {
    Assert.isTrue(tasks.containsKey(aTask.getId()),"Unkown task: " + aTask.getId());
    tasks.put(aTask.getId(), aTask);
  }
  
  @Override
  public JobStatus getStatus() {
    return status;
  }
  
  @Override
  public void setStatus (JobStatus aStatus) {
    if(aStatus == JobStatus.COMPLETED) {
      Assert.isTrue(status==JobStatus.STARTED,String.format("Job %s is %s and so can not be COMPLETED", id,status));
      status = JobStatus.COMPLETED;
      dateCompleted = new Date();
    }
    else if (aStatus == JobStatus.STARTED) {
      Assert.isTrue(status==JobStatus.CREATED||status==JobStatus.FAILED||status==JobStatus.STOPPED,String.format("Job %s is %s and so can not be STARTED", id,status));
      status = JobStatus.STARTED;
      dateStarted = new Date();
    }
    else {
      throw new IllegalArgumentException("Can't handle status: " + aStatus);
    }
  }

  @Override
  public Date getDateCreated() {
    return dateCreated;
  }

  @Override
  public Date getDateStarted() {
    return dateStarted;
  }
  
  @Override
  public Date getDateCompleted() {
    return dateCompleted;
  }
  
}
