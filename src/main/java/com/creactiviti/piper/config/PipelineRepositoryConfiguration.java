/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import java.util.Arrays;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.creactiviti.piper.core.pipeline.FileSystemPipelineRepository;
import com.creactiviti.piper.core.pipeline.GitPipelineRepository;
import com.creactiviti.piper.core.pipeline.PipelineRepositoryChain;

@Configuration
@EnableConfigurationProperties(PiperProperties.class)
public class PipelineRepositoryConfiguration {

  @Bean
  @Primary
  PipelineRepositoryChain pipelineRepository (PiperProperties piperProperties) {
    return new PipelineRepositoryChain(Arrays.asList(
      fileSystemPipelineRepository(),
      gitPipelineRepository(piperProperties)
    ));
  }
  
  @Bean
  GitPipelineRepository gitPipelineRepository (PiperProperties piperProperties) {
    GitPipelineRepository gitPipelineRepository = new GitPipelineRepository();
    gitPipelineRepository.setUrl(piperProperties.getPipelineRepository().getGit().getUrl());
    gitPipelineRepository.setSearchPaths(piperProperties.getPipelineRepository().getGit().getSearchPaths());
    return gitPipelineRepository;
  }
  
  @Bean
  FileSystemPipelineRepository fileSystemPipelineRepository () {
    return new FileSystemPipelineRepository();
  }
  
  
  
}
