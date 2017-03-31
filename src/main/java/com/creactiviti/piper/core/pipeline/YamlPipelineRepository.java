/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.pipeline;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import com.creactiviti.piper.core.task.MutableTask;
import com.creactiviti.piper.core.task.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Throwables;

public abstract class YamlPipelineRepository implements PipelineRepository  {

  protected ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
  
  protected Pipeline parsePipeline (IdentifiableResource aResource) {
    Map<String,Object> yamlMap = parse (aResource);
    String id = aResource.getId();
    String name = (String)yamlMap.get("name");
    List<Task> tasks = (List<Task>) yamlMap.get("tasks");
    return new SimplePipeline(id, name, tasks);
  }
  
  private Map<String,Object> parse (Resource aResource) {
    try (InputStream in = aResource.getInputStream()){
      String yaml = IOUtils.toString(in);
      Map<String,Object> yamlMap = mapper.readValue(yaml, Map.class);
      List<Map<String,Object>> rawTasks = (List<Map<String, Object>>) yamlMap.get("tasks");
      List<Task> tasks = rawTasks.stream().map(rt -> new MutableTask(rt)).collect(Collectors.toList());
      yamlMap.put("tasks", tasks);
      return yamlMap;
    }
    catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

}
