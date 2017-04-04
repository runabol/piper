/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Predicate;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;

/**
 * Configuration annotation for a conditional element that depends on the value of a
 * {@link Predicate}.
 * 
 * @author Arik Cohen
 * @since Apr 3, 2017
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Conditional(OnPredicateCondition.class)
public @interface ConditionalOnPredicate {

  /**
   * The {@link Predicate} to evaluate. Implementations should return {@code true} if the
   * condition passes or {@code false} if it fails.
   * @return the {@link Predicate}
   */
  Class<? extends Predicate<ConditionContext>> value (); 
  
}
