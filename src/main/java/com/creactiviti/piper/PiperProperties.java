package com.creactiviti.piper;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "piper")
public class PiperProperties {

  private SerializationProperties serialization;

  public SerializationProperties getSerialization() {
    return serialization;
  }
  
  public void setSerialization(SerializationProperties aSerialization) {
    serialization = aSerialization;
  }
  
}
