/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.HashMap;
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
    return new MutableJobTask(newMap);
  }

  private Map<String, Object> evaluateInternal(Map<String, Object> aMap, Context aContext) {
    StandardEvaluationContext context = new StandardEvaluationContext(aContext);
    context.addPropertyAccessor(new MapPropertyAccessor());
    Map<String,Object> newMap = new HashMap<String, Object>();
    for(Entry<String,Object> entry : aMap.entrySet()) {
      if(entry.getValue() instanceof String) {
        Expression expression = parser.parseExpression((String)entry.getValue(),new TemplateParserContext("${","}"));
        newMap.put(entry.getKey(), expression.getValue(context));
      }
      else {
        newMap.put(entry.getKey(), entry.getValue());
      }
    }
    return newMap;
  }
  
  public static void main (String[] args) {
    ExpressionParser parser = new SpelExpressionParser();
    String randomPhrase = parser.parseExpression("random number is #{joe}", new TemplateParserContext()).getValue(String.class);
    System.out.println(randomPhrase);
  }

}
