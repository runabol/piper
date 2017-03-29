package com.creactiviti.piper.core.pipeline;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCacheManager;

import com.creactiviti.piper.cache.Clearable;
import com.creactiviti.piper.core.Task;
import com.creactiviti.piper.core.task.MutableTask;
import com.creactiviti.piper.git.GitOperations;
import com.creactiviti.piper.git.JGitTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Throwables;

public class GitPipelineRepository implements PipelineRepository, Clearable  {

  private String url;
  private String[] searchPaths;
  private GitOperations git = new JGitTemplate();
  private CacheManager cacheManager = new GuavaCacheManager ();
  
  private static final String CACHE_ALL = GitPipelineRepository.class.getName()+".all";
  private static final String CACHE_ONE = GitPipelineRepository.class.getName()+".one";
  
  @Override
  public List<Pipeline> findAll () {
    synchronized(this) {
      Cache cache = cacheManager.getCache(CACHE_ALL);
      if(cache.get(CACHE_ALL) != null) {
        return (List<Pipeline>) cache.get(CACHE_ALL).get();
      }
      List<GitResource> resources = git.getHeadFiles(url, searchPaths);
      List<Pipeline> pipelines = resources.stream()
                                          .map(r -> readPipeline(r))
                                          .collect(Collectors.toList());
      cache.put(CACHE_ALL, pipelines);
      return pipelines;
    }
  }

  @Override
  public Pipeline findOne (String aId) {
    synchronized(this) {
      Cache oneCache = cacheManager.getCache(CACHE_ONE);
      if(oneCache.get(aId)!=null) {
        return (Pipeline) oneCache.get(aId).get();
      }
      Cache allCache = cacheManager.getCache(CACHE_ALL);
      if(allCache.get(CACHE_ALL) != null) {
        List<Pipeline> pipelines = (List<Pipeline>) allCache.get(CACHE_ALL).get();
        for(Pipeline p : pipelines) {
          if(p.getId().equals(aId)) {
            return p;
          }
        }
      }
      GitResource resource = git.getFile(url, aId);
      Pipeline pipeline = readPipeline(resource);
      oneCache.put(aId, pipeline);
      return pipeline;
    }
  }
  
  private Pipeline readPipeline (GitResource aResource) {
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

  public void setUrl(String aUrl) {
    url = aUrl;
  }

  public void setSearchPaths(String[] aSearchPaths) {
    searchPaths = aSearchPaths;
  }
  
  public void setGitOperations(GitOperations aGit) {
    git = aGit;
  }
  
  public void setCacheManager(CacheManager aCacheManager) {
    cacheManager = aCacheManager;
  }

  @Override
  public void clear() {
    cacheManager.getCacheNames().forEach(cn->cacheManager.getCache(cn).clear());
  }

}
