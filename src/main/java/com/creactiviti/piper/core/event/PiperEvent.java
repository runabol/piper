/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.event;

import java.util.Collections;
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

  public PiperEvent() {
    super(Collections.EMPTY_MAP);
  }
  
  private PiperEvent(Map<String,Object> aSource) {
    super(aSource);
  }

  public String getType () {
    return getRequiredString("type");
  }
  
  public static PiperEvent of (String aType) {
    return of (aType, Collections.EMPTY_MAP);
  }
  
  public static PiperEvent of (String aType, String aKey, String aValue) {
    Assert.notNull(aKey,"key must not be null");
    Assert.notNull(aValue,"value for " + aKey + " must not be null");
    return of(aType,ImmutableMap.of(aKey, aValue));
  }
  
  public static PiperEvent of (String aType, Map<String, Object> aProperties) {
    Assert.notNull(aType,"event type must not be null");
    Map<String,Object> source = new HashMap<>(ImmutableMap.of("id",UUIDGenerator.generate(),"type", aType));
    source.putAll(aProperties);
    return new PiperEvent(source);
  }
  
}
