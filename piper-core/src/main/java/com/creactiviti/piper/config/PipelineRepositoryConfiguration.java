/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

import com.creactiviti.piper.core.pipeline.FileSystemPipelineRepository;
import com.creactiviti.piper.core.pipeline.GitPipelineRepository;
import com.creactiviti.piper.core.pipeline.PipelineRepository;
import com.creactiviti.piper.core.pipeline.PipelineRepositoryChain;

@Configuration
@EnableConfigurationProperties(PiperProperties.class)
public class PipelineRepositoryConfiguration {

  @Bean
  @Primary
  PipelineRepositoryChain pipelineRepository (List<PipelineRepository> aRepositories) {
    return new PipelineRepositoryChain(aRepositories);
  }
  
  @Bean
  @Order(2)
  @ConditionalOnProperty(name="piper.pipeline-repository.git.enabled",havingValue="true")
  GitPipelineRepository gitPipelineRepository (PiperProperties piperProperties) {
    GitPipelineRepository gitPipelineRepository = new GitPipelineRepository();
    gitPipelineRepository.setUrl(piperProperties.getPipelineRepository().getGit().getUrl());
    gitPipelineRepository.setSearchPaths(piperProperties.getPipelineRepository().getGit().getSearchPaths());
    return gitPipelineRepository;
  }
  
  @Bean
  @Order(1)
  FileSystemPipelineRepository fileSystemPipelineRepository () {
    return new FileSystemPipelineRepository();
  }
  
  
  
}
