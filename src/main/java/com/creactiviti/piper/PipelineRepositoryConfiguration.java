package com.creactiviti.piper;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.pipeline.GitPipelineRepository;

@Configuration
@EnableConfigurationProperties(PiperProperties.class)
public class PipelineRepositoryConfiguration {

  @Bean
  GitPipelineRepository gitPipelineRepository (PiperProperties piperProperties) {
    GitPipelineRepository gitPipelineRepository = new GitPipelineRepository();
    gitPipelineRepository.setUrl(piperProperties.getPipelineRepository().getGit().getUrl());
    gitPipelineRepository.setSearchPaths(piperProperties.getPipelineRepository().getGit().getSearchPaths());
    return gitPipelineRepository;
  }
  
}
