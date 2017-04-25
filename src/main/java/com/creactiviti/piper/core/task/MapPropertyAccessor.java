/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Map;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;

/**
 * Simple {@link PropertyAccessor} that can access {@link Map} properties.
 *
 * @author Arik Cohen
 * @since Mar 31, 2017
 */
public class MapPropertyAccessor implements PropertyAccessor {

  private final String prefix;
  private final String suffix;
  
  public MapPropertyAccessor(String aPrefix, String aSuffix) {
    prefix = aPrefix;
    suffix = aSuffix;
  }
  
  @Override
  public Class<?>[] getSpecificTargetClasses() {
    return new Class<?>[]{Map.class};
  }

  @Override
  public boolean canRead(EvaluationContext aContext, Object aTarget, String aName) throws AccessException {
    return aTarget instanceof Map;
  }

  @Override
  public TypedValue read(EvaluationContext aContext, Object aTarget, String aName) throws AccessException {
    Map<String,Object> map = (Map<String, Object>) aTarget;
    if(map.containsKey(aName)) {
      Object value = map.get(aName);
      return new TypedValue(value, TypeDescriptor.forObject(value));
    }
    else {
      throw new SpelEvaluationException(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE, aName, aTarget.getClass());
    }
  }

  @Override
  public boolean canWrite(EvaluationContext aContext, Object aTarget, String aName) throws AccessException {
    return false;
  }

  @Override
  public void write(EvaluationContext aContext, Object aTarget, String aName, Object aNewValue) throws AccessException {
    throw new UnsupportedOperationException();
  }

}
