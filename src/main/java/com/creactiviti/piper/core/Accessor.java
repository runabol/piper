package com.creactiviti.piper.core;

public interface Accessor {

  Object get (String aKey);
  
  <T> T get (String aKey, Class<T> aReturnType);
  
  String getString (String aKey);
  
  String getString (String aKey, String aDefaultValue);
  
  Long getLong (String aKey);
  
  Long getLong (String aKey, long aDefaultValue);

  Double getDouble (String aKey);
  
  Double getDouble (String aKey, double aDefaultValue);
  
  Integer getInteger (String aKey);
  
  Integer getInteger (String aKey, int aDefaultValue);
  
}
