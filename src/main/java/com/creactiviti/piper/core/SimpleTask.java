package com.creactiviti.piper.core;

import java.util.Map;

public class SimpleTask implements Task {

  private final MapObject map;
  
  public SimpleTask(Map<String, Object> aSource) {
    map = new MapObject(aSource);
  }
  
  @Override
  public Object get(Object aKey) {
    return map.get(aKey);
  }

  @Override
  public <T> T get(Object aKey, Class<T> aReturnType) {
    return map.get(aKey,aReturnType);
  }

  @Override
  public String getString(Object aKey) {
    return map.getString(aKey);
  }

  @Override
  public String getString(Object aKey, String aDefaultValue) {
    return map.getString(aKey, aDefaultValue);
  }

  @Override
  public Long getLong(Object aKey) {
    return map.getLong(aKey);
  }

  @Override
  public Long getLong(Object aKey, long aDefaultValue) {
    return map.getLong(aKey);
  }

  @Override
  public Double getDouble(Object aKey) {
    return map.getDouble(aKey);
  }

  @Override
  public Double getDouble(Object aKey, double aDefaultValue) {
    return map.getDouble(aKey, aDefaultValue);
  }

  @Override
  public Integer getInteger(Object aKey) {
    return map.getInteger(aKey);
  }

  @Override
  public Integer getInteger(Object aKey, int aDefaultValue) {
    return map.getInteger(aKey,aDefaultValue);
  }

  @Override
  public <T> T get(Object aKey, Class<T> aReturnType, T aDefaultValue) {
    return map.get(aKey, aReturnType, aDefaultValue);
  }
  
  @Override
  public String toString() {
    return map.toString();
  }

}
