/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.pipeline;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.google.common.base.Throwables;

public class FileSystemPipelineRepository extends YamlPipelineRepository {

  @Override
  public List<Pipeline> findAll () {
    try {
      ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
      Resource[] resources = resolver.getResources("file:pipelines/**/*.yaml");
      return Arrays.asList(resources)
                   .stream()
                   .map(r -> read(r))
                   .collect(Collectors.toList());
    }
    catch(IOException e) {
      throw Throwables.propagate(e);
    }
  }

  private Pipeline read (Resource aResource) {
    try {
      String uri = aResource.getURI().toString();
      String id = uri.substring(uri.lastIndexOf("pipelines/")+10,uri.lastIndexOf('.'));
      return parsePipeline(new IdentifiableResource(id, aResource));
    }
    catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public Pipeline findOne (String aId) {
    List<Pipeline> pipelines = findAll ();
    Optional<Pipeline> findFirst = pipelines.stream().filter(p->p.getId().equals(aId)).findFirst();
    if(findFirst.isPresent()) {
      return findFirst.get();
    }
    throw new IllegalArgumentException("Unknown pipeline: " + aId);
  }

}
