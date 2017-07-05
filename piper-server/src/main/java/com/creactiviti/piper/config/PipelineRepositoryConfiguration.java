
package com.creactiviti.piper.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

import com.creactiviti.piper.core.pipeline.GitPipelineRepository;
import com.creactiviti.piper.core.pipeline.PipelineRepository;
import com.creactiviti.piper.core.pipeline.PipelineRepositoryChain;
import com.creactiviti.piper.core.pipeline.ResourceBasedPipelineRepository;

@Configuration
@EnableConfigurationProperties(PiperProperties.class)
public class PipelineRepositoryConfiguration {

  @Bean
  @Primary
  PipelineRepositoryChain pipelineRepository (List<PipelineRepository> aRepositories) {
    return new PipelineRepositoryChain(aRepositories);
  }
     
  @Bean
  @Order(1)
  ResourceBasedPipelineRepository resourceBasedPipelineRepository () {
    return new ResourceBasedPipelineRepository();
  }
  
  @Bean
  @Order(2)
  @ConditionalOnProperty(name="piper.pipeline-repository.filesystem.enabled",havingValue="true")
  ResourceBasedPipelineRepository fileSystemBasedPipelineRepository (@Value("${piper.pipeline-repository.filesystem.base-path}") String aBasePath) {
    return new ResourceBasedPipelineRepository(String.format("file:%s", aBasePath));
  }
  
  @Bean
  @Order(3)
  @ConditionalOnProperty(name="piper.pipeline-repository.git.enabled",havingValue="true")
  GitPipelineRepository gitPipelineRepository (PiperProperties piperProperties) {
    GitPipelineRepository gitPipelineRepository = new GitPipelineRepository();
    gitPipelineRepository.setUrl(piperProperties.getPipelineRepository().getGit().getUrl());
    gitPipelineRepository.setSearchPaths(piperProperties.getPipelineRepository().getGit().getSearchPaths());
    return gitPipelineRepository;
  }  
  
}
