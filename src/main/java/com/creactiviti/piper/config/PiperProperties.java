/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "piper")
public class PiperProperties {

  private SerializationProperties serialization;
  private PersistenceProperties persistence;
  private PipelineRepositoryProperties pipelineRepository;
  private MessengerProperties messenger;
  private String[] roles;

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
  
  public PipelineRepositoryProperties getPipelineRepository() {
    return pipelineRepository;
  }
  
  public void setPipelineRepository(PipelineRepositoryProperties aPipelineRepository) {
    pipelineRepository = aPipelineRepository;
  }
  
  public void setRoles(String[] aRoles) {
    roles = aRoles;
  }
  
  public String[] getRoles() {
    return roles;
  }
  
  public MessengerProperties getMessenger() {
    return messenger;
  }
  
  public void setMessenger(MessengerProperties aMessenger) {
    messenger = aMessenger;
  }
  
}
