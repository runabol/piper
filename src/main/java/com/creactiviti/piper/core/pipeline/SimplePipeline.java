/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.pipeline;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.creactiviti.piper.core.Accessor;
import com.creactiviti.piper.core.MapObject;
import com.creactiviti.piper.core.task.PipelineTask;

public class SimplePipeline extends MapObject implements Pipeline {

  public SimplePipeline(Map<String, Object> aSource) {
    super(aSource);
  }

  @Override
  public String getId() {
    return getString("id");
  }

  @Override
  public String getName() {
    return getString("name");
  }

  @Override
  public List<PipelineTask> getTasks() {
    return getList("tasks", PipelineTask.class);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public List<Accessor> getInputs() {
    return getList("inputs",Accessor.class,Collections.EMPTY_LIST);
  }
}
