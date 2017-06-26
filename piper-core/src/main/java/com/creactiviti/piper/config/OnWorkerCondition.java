/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * A Condition that evaluates if piper.roles property 
 * contains the value <code>worker</code>.
 *
 * @author Arik Cohen
 * @see ConditionalOnWorker
 */
public class OnWorkerCondition extends SpringBootCondition {

  @Override
  public ConditionOutcome getMatchOutcome(ConditionContext aContext, AnnotatedTypeMetadata aMetadata) {
    String property = aContext.getEnvironment().getProperty("piper.worker.enabled");
    boolean result = Boolean.valueOf(property);
    return new ConditionOutcome(result,ConditionMessage.forCondition(ConditionalOnWorker.class, "(" + getClass().getName() + ")").resultedIn(result));
  }

}
