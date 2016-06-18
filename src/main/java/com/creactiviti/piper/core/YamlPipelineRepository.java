package com.creactiviti.piper.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Throwables;

@Component
public class YamlPipelineRepository implements PipelineRepository  {

  @Override
  public List<Pipeline> findAll() {
    try {
      ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
      Resource[] resources = resolver.getResources("classpath:pipelines/**/*.yaml");
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
      List<Map<String,Object>> tasks = (List<Map<String, Object>>) yamlMap.get("tasks");
      return new SimplePipeline(aResource.getFilename(), (String)yamlMap.get("name"), new ArrayList<Task>());
    }
    catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public Pipeline find(String aId) {
    findAll();
    return null;
  }

}
