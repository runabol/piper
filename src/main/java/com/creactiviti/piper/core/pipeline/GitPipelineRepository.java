/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.pipeline;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCacheManager;

import com.creactiviti.piper.cache.Clearable;
import com.creactiviti.piper.git.GitOperations;
import com.creactiviti.piper.git.JGitTemplate;

public class GitPipelineRepository extends YamlPipelineRepository implements Clearable  {

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
      List<IdentifiableResource> resources = git.getHeadFiles(url, searchPaths);
      List<Pipeline> pipelines = resources.stream()
                                          .map(r -> parsePipeline(r))
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
      IdentifiableResource resource = git.getFile(url, aId);
      Pipeline pipeline = parsePipeline(resource);
      oneCache.put(aId, pipeline);
      return pipeline;
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
