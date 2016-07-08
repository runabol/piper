package com.creactiviti.piper.core.pipeline;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.creactiviti.piper.core.Task;

public class SimplePipeline implements Pipeline {

  private final String id;
  private final String name;
  private final List<Task> tasks;
  
  public SimplePipeline(String aId, String aName, List<Task> aTasks) {
    id = aId;
    name = aName;
    tasks = aTasks;
  }
  
  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<Task> getTasks() {
    return tasks;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
