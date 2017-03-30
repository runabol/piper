package com.creactiviti.piper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "piper")
public class PiperProperties {

  private SerializationProperties serialization;
  private PersistenceProperties persistence;
  private PipelineRepository pipelineRepository;

  public SerializationProperties getSerialization() {
    return serialization;
  }
  
  public void setSerialization(SerializationProperties aSerialization) {
    serialization = aSerialization;
  }
  
  public PersistenceProperties getPersistence() {
    return persistence;
  }
  
  public void setPersistence(PersistenceProperties aPersistence) {
    persistence = aPersistence;
  }
  
  public PipelineRepository getPipelineRepository() {
    return pipelineRepository;
  }
  
  public void setPipelineRepository(PipelineRepository aPipelineRepository) {
    pipelineRepository = aPipelineRepository;
  }
  
}
