/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import java.util.Map;
import java.util.function.Predicate;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.google.common.base.Throwables;

/**
 * A Condition that evaluates a Predicate.
 *
 * @author Arik Cohen
 * @see ConditionalOnPredicate
 */
public class OnPredicateCondition extends SpringBootCondition {

  @Override
  public ConditionOutcome getMatchOutcome(ConditionContext aContext, AnnotatedTypeMetadata aMetadata) {
    Map<String, Object> attrs = aMetadata.getAnnotationAttributes(ConditionalOnPredicate.class.getName());
    Class<Predicate<ConditionContext>> predicateClass = (Class<Predicate<ConditionContext>>) attrs.get("value");
    try {
      Predicate predicate = predicateClass.newInstance();
      boolean test = predicate.test(aContext);
      return new ConditionOutcome(test,ConditionMessage.forCondition(ConditionalOnPredicate.class, "(" + predicateClass.getName() + ")").resultedIn(test));
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

}
