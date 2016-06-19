package com.creactiviti.piper.core;

public interface Accessor {

  Object get (Object aKey);
  
  <T> T get (Object aKey, Class<T> aReturnType);
  
  <T> T get (Object aKey, Class<T> aReturnType, T aDefaultValue);
  
  String getString (Object aKey);
  
  String getString (Object aKey, String aDefaultValue);
  
  Long getLong (Object aKey);
  
  Long getLong (Object aKey, long aDefaultValue);

  Double getDouble (Object aKey);
  
  Double getDouble (Object aKey, double aDefaultValue);
  
  Integer getInteger (Object aKey);
  
  Integer getInteger (Object aKey, int aDefaultValue);
  
}
