package com.creactiviti.piper.core.pipeline;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import com.creactiviti.piper.core.Task;
import com.creactiviti.piper.core.task.MutableTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Throwables;

public abstract class YamlPipelineRepository implements PipelineRepository  {

  protected ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
  
  protected Map<String,Object> parseYaml (Resource aResource) {
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
