package com.creactiviti.piper;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.pipeline.GitPipelineRepository;

@Configuration
@EnableConfigurationProperties(PiperProperties.class)
public class PipelineRepositoryConfiguration {

  @Bean
  GitPipelineRepository yamlPipelineRepository (PiperProperties piperProperties) {
    GitPipelineRepository yamlPipelineRepository = new GitPipelineRepository();
    yamlPipelineRepository.setUrl(piperProperties.getPipelineRepository().getGit().getUrl());
    yamlPipelineRepository.setSearchPath(piperProperties.getPipelineRepository().getGit().getSearchPath());
    return yamlPipelineRepository;
  }
  
}
