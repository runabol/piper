package com.creactiviti.piper.core.pipeline;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import com.creactiviti.piper.core.Task;
import com.creactiviti.piper.core.task.MutableTask;
import com.creactiviti.piper.git.GitOperations;
import com.creactiviti.piper.git.JGitTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Throwables;

public class GitPipelineRepository implements PipelineRepository  {

  private String url;
  private String[] searchPaths;
  private GitOperations git = new JGitTemplate();
  
  @Override
  public List<Pipeline> findAll () {
    List<GitResource> resources = getResources();
    return resources.stream()
                    .map(r -> read(r))
                    .collect(Collectors.toList());
  }

  private Pipeline read (GitResource aResource) {
    try (InputStream in = aResource.getInputStream()) {
      String yaml = IOUtils.toString(in);
      ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
      Map<String,Object> yamlMap = mapper.readValue(yaml, Map.class);
      List<Map<String,Object>> rawTasks = (List<Map<String, Object>>) yamlMap.get("tasks");
      List<Task> tasks = rawTasks.stream().map(rt -> new MutableTask(rt)).collect(Collectors.toList());
      String id = aResource.getId();
      String name = (String)yamlMap.get("name");
      return new SimplePipeline(id, name, tasks);
    }
    catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  private List<GitResource> getResources () {
   return git.getHeadFiles(url, searchPaths);
  }

  @Override
  public Pipeline findOne (String aId) {
    GitResource resource = git.getFile(url, aId);
    return read(resource);
  }

  public void setUrl(String aUrl) {
    url = aUrl;
  }

  public void setSearchPaths(String[] aSearchPaths) {
    searchPaths = aSearchPaths;
  }
  
  public void setGitOperations(GitOperations aGit) {
    git = aGit;
  }

}
