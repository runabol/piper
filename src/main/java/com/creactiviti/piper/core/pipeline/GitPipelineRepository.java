package com.creactiviti.piper.core.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.Task;
import com.creactiviti.piper.core.task.MutableTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Throwables;
import com.google.common.io.Files;

public class GitPipelineRepository implements PipelineRepository  {

  private String url;
  private String searchPath;
  
  @Override
  public List<Pipeline> findAll () {
    try {
      Resource rootResource = resolve(url+"/"+searchPath+"/**");
      File rootDir = rootResource.getFile();
      File[] listFiles = rootDir.listFiles();
      List<Resource> resources = new ArrayList<>();
      for(File f : listFiles) {
        resources.add(new FileSystemResource(f));
      }
      return resources.stream()
                      .map(r -> read(r))
                      .collect(Collectors.toList());
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

  private Resource resolve (String aLocation) {
    try {
      File tempDir = Files.createTempDir();
      String baseUri = aLocation.substring(0, aLocation.indexOf(".git")+4);
      Git.cloneRepository()
         .setURI(baseUri)
         .setDirectory(tempDir)
         .call();     
      return ( new FileSystemResource( new File(tempDir,searchPath) ) );
    }
    catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public Pipeline findOne (String aId) {
    Resource resource = resolve(url+"/"+aId);
    return read(resource);
  }

  public void setUrl(String aUrl) {
    url = aUrl;
  }
  
  public void setSearchPath(String aSearchPath) {
    searchPath = aSearchPath;
  }

}
