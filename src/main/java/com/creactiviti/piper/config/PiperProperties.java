/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "piper")
public class PiperProperties {

  private SerializationProperties serialization;
  private PersistenceProperties persistence;
  private PipelineRepositoryProperties pipelineRepository;
  private MessengerProperties messenger;
  private CoordinatorProperties coordinator;
  private WorkerProperties worker;

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
  
  public MessengerProperties getMessenger() {
    return messenger;
  }
  
  public void setMessenger(MessengerProperties aMessenger) {
    messenger = aMessenger;
  }

  public CoordinatorProperties getCoordinator() {
    return coordinator;
  }
  
  public void setCoordinator(CoordinatorProperties aCoordinator) {
    coordinator = aCoordinator;
  }
  
  public WorkerProperties getWorker() {
    return worker;
  }
  
  public void setWorker(WorkerProperties aWorker) {
    worker = aWorker;
  }
  
}
