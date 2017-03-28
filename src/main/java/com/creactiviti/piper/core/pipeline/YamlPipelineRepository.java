package com.creactiviti.piper.core.pipeline;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.Task;
import com.creactiviti.piper.core.task.MutableTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Throwables;

public class YamlPipelineRepository implements PipelineRepository  {

  private final static String DEFAULT_PATH = "file:pipelines/**/*.yaml";
  private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(new CustomResourceLoader());
  private String path = DEFAULT_PATH;
  
  @Override
  public List<Pipeline> findAll () {
    try {
      Resource[] resources = resolver.getResources(path);
      return Arrays.asList(resources).stream().map(r -> read(r)).collect(Collectors.toList());
    }
    catch(IOException e) {
      throw Throwables.propagate(e);
    }
  }

  private Pipeline read (Resource aResource) {
    try {
      String yaml = IOUtils.toString(aResource.getInputStream());
      ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
      Map<String,Object> yamlMap = mapper.readValue(yaml, Map.class);
      List<Map<String,Object>> rawTasks = (List<Map<String, Object>>) yamlMap.get("tasks");
      List<Task> tasks = rawTasks.stream().map(rt -> new MutableTask(rt)).collect(Collectors.toList());
      String id = (String)yamlMap.get("id");
      String name = (String)yamlMap.get("name");
      Assert.notNull(id,"id not defined in pipline");
      Assert.notNull(id,"name not defined in pipline");
      return new SimplePipeline(id, name, tasks);
    }
    catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public Pipeline findOne (String aId) {
    List<Pipeline> pipelines = findAll ();
    Optional<Pipeline> findFirst = pipelines.stream().filter(p->p.getId().equals(aId)).findFirst();
    return findFirst.isPresent()?findFirst.get():null;
  }
  
  public void setPath(String aPath) {
    path = aPath;
  }

}
