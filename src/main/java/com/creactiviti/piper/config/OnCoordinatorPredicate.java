/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import java.util.Arrays;
import java.util.function.Predicate;

import org.springframework.context.annotation.ConditionContext;

public class OnCoordinatorPredicate implements Predicate<ConditionContext> {

  @Override
  public boolean test (ConditionContext aContext) {
    String property = aContext.getEnvironment().getProperty("piper.roles");
    return property!=null&&Arrays.asList(property.split(",")).contains("coordinator");
  }

}
