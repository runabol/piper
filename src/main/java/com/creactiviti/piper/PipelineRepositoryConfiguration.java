package com.creactiviti.piper;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.pipeline.YamlPipelineRepository;

@Configuration
@EnableConfigurationProperties(PiperProperties.class)
public class PipelineRepositoryConfiguration {

  @Bean
  YamlPipelineRepository yamlPipelineRepository (PiperProperties piperProperties) {
    YamlPipelineRepository yamlPipelineRepository = new YamlPipelineRepository();
    yamlPipelineRepository.setPath(piperProperties.getPipelineRepository().getPath());
    return yamlPipelineRepository;
  }
  
}
