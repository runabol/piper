/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.creactiviti.piper.core.context.Context;
import com.creactiviti.piper.core.job.MutableJobTask;

/**
 * a {@link TaskEvaluator} implemenation which is based on 
 * Spring Expression Language for resolving expressions.
 * 
 * @author Arik Cohen
 * @since Mar 31, 2017
 */
public class SpelTaskEvaluator implements TaskEvaluator {

  private final ExpressionParser parser = new SpelExpressionParser();
  
  @Override
  public JobTask evaluate(JobTask aJobTask, Context aContext) {
    Map<String, Object> map = aJobTask.asMap();
    Map<String, Object> newMap = evaluateInternal(map,aContext);
    return MutableJobTask.createFromMap(newMap);
  }

  private Map<String, Object> evaluateInternal(Map<String, Object> aMap, Context aContext) {
    Map<String,Object> newMap = new HashMap<String, Object>();
    for(Entry<String,Object> entry : aMap.entrySet()) {
      newMap.put(entry.getKey(), evaluate(entry.getValue(),aContext));
    }
    return newMap;
  }
  
  private Object evaluate (Object aValue, Context aContext) {
    StandardEvaluationContext context = createEvaluationContext(aContext);
    if(aValue instanceof String) {
      Expression expression = parser.parseExpression((String)aValue,new TemplateParserContext("${","}"));
      return(expression.getValue(context));
    }
    else if (aValue instanceof List) {
      List<Object> evaluatedlist = new ArrayList<>();
      List<Object> list = (List<Object>) aValue;
      for(Object item : list) {
        evaluatedlist.add(evaluate(item, aContext));
      }
      return evaluatedlist;
    }
    else if (aValue instanceof Map) {
      return evaluateInternal((Map<String, Object>) aValue, aContext);
    }
    return aValue;
  }

  private StandardEvaluationContext createEvaluationContext(Context aContext) {
    StandardEvaluationContext context = new StandardEvaluationContext(aContext);
    context.addPropertyAccessor(new MapPropertyAccessor());
    return context;
  }
  
}
