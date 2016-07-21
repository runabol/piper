package com.creactiviti.piper.core.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.Task;
import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.uuid.UUIDFactory;

public class SimpleJob implements Job {

  private final String id = UUIDFactory.create();
  private final Pipeline pipeline;
  private JobStatus status = JobStatus.CREATED;
  private final Map<String,JobTask> tasks = new LinkedHashMap<>();
  private int nextTask = 0;
  private final Date creationDate = new Date();
  private Date completionDate;
  private Date startDate;
  
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
  public JobStatus getStatus() {
    return status;
  }
  
  public void setStatus (JobStatus aStatus) {
    if(aStatus == JobStatus.COMPLETED) {
      Assert.isTrue(status==JobStatus.STARTED,String.format("Job %s is %s and so can not be COMPLETED", id,status));
      status = JobStatus.COMPLETED;
      completionDate = new Date();
    }
    else if (aStatus == JobStatus.STARTED) {
      Assert.isTrue(status==JobStatus.CREATED||status==JobStatus.FAILED||status==JobStatus.STOPPED,String.format("Job %s is %s and so can not be STARTED", id,status));
      status = JobStatus.STARTED;
      startDate = new Date();
    }
    else {
      throw new IllegalArgumentException("Can't handle status: " + aStatus);
    }
  }

  @Override
  public Date getCreationDate() {
    return creationDate;
  }
  
  @Override
  public Date getStartDate() {
    return startDate;
  }
  
  @Override
  public Date getCompletionDate() {
    return completionDate;
  }
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
  
}
