/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.event;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.Accessor;
import com.creactiviti.piper.core.MapObject;
import com.creactiviti.piper.core.uuid.UUIDGenerator;
import com.google.common.collect.ImmutableMap;

/**
 * 
 * @author Arik Cohen
 * @since Apr 8, 2017
 */
@SuppressWarnings("serial")
public class PiperEvent extends ApplicationEvent implements Accessor {

  private PiperEvent() {
    super(new MapObject());
  }
  
  private PiperEvent(Map<String,Object> aSource) {
    super(new MapObject(aSource));
  }
  
  @Override
  public MapObject getSource() {
    Object source = super.getSource();
    if(source instanceof MapObject) {
      return (MapObject) source;
    }
    return new MapObject((Map<String,Object>)source);
  }
  
  public String getType () {
    return getSource().getRequiredString("type");
  }
  
  public Date getCreateTime () {
    return getDate("createTime");
  }

  @Override
  public <T> T get(Object aKey) {
    return (T) getSource().get(aKey);
  }

  @Override
  public boolean containsKey(Object aKey) {
    return getSource().containsKey(aKey);
  }

  public <T> T get(Object aKey, Class<T> aReturnType) {
    return getSource().get(aKey, aReturnType);
  }

  public <T> T get(Object aKey, Class<T> aReturnType, T aDefaultValue) {
    return getSource().get(aKey, aReturnType, aDefaultValue);
  }

  public String getString(Object aKey) {
    return getSource().getString(aKey);
  }

  public <T> T[] getArray(Object aKey, Class<T> aElementType) {
    return getSource().getArray(aKey, aElementType);
  }

  public Map<String, Object> getMap(Object aKey) {
    return getSource().getMap(aKey);
  }

  public Map<String, Object> getMap(Object aKey, Map<String, Object> aDefault) {
    return getSource().getMap(aKey, aDefault);
  }

  public String getRequiredString(Object aKey) {
    return getSource().getRequiredString(aKey);
  }

  public <T> T getRequired(Object aKey) {
    return getSource().getRequired(aKey);
  }

  public String getString(Object aKey, String aDefaultValue) {
    return getSource().getString(aKey, aDefaultValue);
  }

  public <T> List<T> getList(Object aKey, Class<T> aElementType) {
    return getSource().getList(aKey, aElementType);
  }

  public <T> List<T> getList(Object aKey, Class<T> aElementType,
      List<T> aDefaultValue) {
    return getSource().getList(aKey, aElementType, aDefaultValue);
  }

  public Long getLong(Object aKey) {
    return getSource().getLong(aKey);
  }

  public long getLong(Object aKey, long aDefaultValue) {
    return getSource().getLong(aKey, aDefaultValue);
  }

  public Double getDouble(Object aKey) {
    return getSource().getDouble(aKey);
  }

  public Double getDouble(Object aKey, double aDefaultValue) {
    return getSource().getDouble(aKey, aDefaultValue);
  }

  public Float getFloat(Object aKey) {
    return getSource().getFloat(aKey);
  }

  public float getFloat(Object aKey, float aDefaultValue) {
    return getSource().getFloat(aKey, aDefaultValue);
  }

  public Integer getInteger(Object aKey) {
    return getSource().getInteger(aKey);
  }

  public int getInteger(Object aKey, int aDefaultValue) {
    return getSource().getInteger(aKey, aDefaultValue);
  }

  public Boolean getBoolean(Object aKey) {
    return getSource().getBoolean(aKey);
  }

  public boolean getBoolean(Object aKey, boolean aDefaultValue) {
    return getSource().getBoolean(aKey, aDefaultValue);
  }

  public Date getDate(Object aKey) {
    return getSource().getDate(aKey);
  }

  public Map<String, Object> asMap() {
    return getSource().asMap();
  }
  
  public static PiperEvent of (String aType) {
    return of (aType, Collections.EMPTY_MAP);
  }
  
  public static PiperEvent of (String aType, String aKey, Object aValue) {
    Assert.notNull(aKey,"key must not be null");
    Assert.notNull(aValue,"value for " + aKey + " must not be null");
    return of(aType,ImmutableMap.of(aKey, aValue));
  }
  
  public static PiperEvent of (String aType, String aKey1, Object aValue1, String aKey2, Object aValue2) {
    Assert.notNull(aKey1,"key must not be null");
    Assert.notNull(aValue1,"value for " + aKey1 + " must not be null");
    Assert.notNull(aKey2,"key must not be null");
    Assert.notNull(aValue2,"value for " + aKey2 + " must not be null");
    return of(aType,ImmutableMap.of(aKey1, aValue1,aKey2,aValue2));
  }
  
  public static PiperEvent of (String aType, Map<String, Object> aProperties) {
    Assert.notNull(aType,"event type must not be null");
    Map<String,Object> source = new HashMap<>(ImmutableMap.of("id",UUIDGenerator.generate(),"type", aType,"createTime",new Date()));
    source.putAll(aProperties);
    return new PiperEvent(source);
  }
  
}
