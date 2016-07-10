package com.creactiviti.piper;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "piper")
public class PiperProperties {

  private SerializationProperties serialization;
  private PersistenceProperties persistence;

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
  
}
