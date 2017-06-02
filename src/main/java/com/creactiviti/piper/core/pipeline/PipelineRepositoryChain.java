/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Jun 2017
 */
package com.creactiviti.piper.core.pipeline;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author Arik Cohen
 * @since Jun 2, 2017
 */
public class PipelineRepositoryChain implements PipelineRepository {

  private final List<PipelineRepository> repositories;

  private Logger logger = LoggerFactory.getLogger(getClass());

  public PipelineRepositoryChain(List<PipelineRepository> aRepositories) {
    Assert.notNull(aRepositories, "'aRepositories' can not be null");
    repositories = aRepositories;
  }

  @Override
  public Pipeline findOne(String aId) {
    for(PipelineRepository repository : repositories) {
      try {
        return repository.findOne(aId);
      }
      catch (Exception e) {
        logger.debug("{}",e.getMessage());
      }
    }
    throw new IllegalArgumentException("Unknown pipeline: " + aId);
  }

  @Override
  public List<Pipeline> findAll() {
    return repositories.stream()
                       .map(r->r.findAll())
                       .flatMap(List::stream)
                       .sorted((a,b)->a.getLabel().compareTo(b.getLabel()))
                       .collect(Collectors.toList());
  }

}
