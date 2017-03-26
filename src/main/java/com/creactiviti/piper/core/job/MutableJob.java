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

public class MutableJob implements Job {

  private final String id;
  private final Pipeline pipeline;
  private final Date creationDate;
  
  private JobStatus status = JobStatus.CREATED;
  private Map<String,JobTask> execution = new LinkedHashMap<>();
  
  private Date completionDate;
  private Date startDate;
  
  /**
   * Constructs a mutable version of a {@link Job}
   * instance.
   * 
   * @param aJob
   */
  public MutableJob (Job aJob) {
    id = aJob.getId();
    pipeline = aJob.getPipeline();
    creationDate = aJob.getCreationDate();
    status = aJob.getStatus();
    aJob.getExecution().forEach(t->execution.put(t.getId(), t));
    completionDate = aJob.getCompletionDate();
    startDate = aJob.getStartDate();
  }
  
  /**
   * Constructs a new {@link Job} instance.
   * 
   * @param aJob
   */
  public MutableJob (Pipeline aPipeline) {
    Assert.notNull(aPipeline,"pipeline must not be null");
    pipeline = aPipeline;
    creationDate = new Date();
    id = UUIDFactory.create();
  }
  
  @Override
  public String getId() {
    return id;
  }
  
  @Override
  public List<JobTask> getExecution() {
    return Collections.unmodifiableList(new ArrayList<JobTask>(execution.values()));
  }
  
  @Override
  public boolean hasMoreTasks() {
    return execution.size() < pipeline.getTasks().size();
  }
  
  public JobTask nextTask() {
    Task task = pipeline.getTasks().get(execution.size());
    MutableJobTask mt = new MutableJobTask (task.toMap());
    execution.put(mt.getId(),mt);
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

  public void updateTask (JobTask aJobTask) {
    Assert.isTrue(execution.containsKey(aJobTask.getId()),"Unkown task: " + aJobTask.getId());
    execution.put(aJobTask.getId(), aJobTask);
  }
  
  @Override
  public Date getCreationDate() {
    return creationDate;
  }
  
  @Override
  public Pipeline getPipeline() {
    return pipeline;
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
