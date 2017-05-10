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
import java.util.Map;

import org.springframework.util.Assert;

import com.creactiviti.piper.core.MapObject;
import com.creactiviti.piper.core.uuid.UUIDGenerator;
import com.google.common.collect.ImmutableMap;

/**
 * 
 * @author Arik Cohen
 * @since Apr 8, 2017
 */
public class PiperEvent extends MapObject {

  private PiperEvent() {
    super(Collections.EMPTY_MAP);
  }
  
  private PiperEvent(Map<String,Object> aSource) {
    super(aSource);
  }

  public String getType () {
    return getRequiredString("type");
  }
  
  public Date getTimestamp () {
    return getDate("timestamp");
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
    Map<String,Object> source = new HashMap<>(ImmutableMap.of("id",UUIDGenerator.generate(),"type", aType,"timestamp",new Date()));
    source.putAll(aProperties);
    return new PiperEvent(source);
  }
  
}
