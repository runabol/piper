/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

public class RoleParser {

  private static final Pattern ROLE_PATTERN = Pattern.compile("([a-zA-Z0-9\\.]+)(\\((\\d+)\\))*");
  
  private static final String DEFAULT_SUFFIX = ".tasks";
  
  static String queueName (String aRole) {
    Matcher qMatcher = ROLE_PATTERN.matcher(aRole);
    Assert.isTrue(qMatcher.matches(), "Invalid role: " + aRole);
    return qMatcher.group(1)+DEFAULT_SUFFIX;
  }
  
  static int concurrency (String aRole) {
    Matcher qMatcher = ROLE_PATTERN.matcher(aRole);
    Assert.isTrue(qMatcher.matches(), "Invalid role: " + aRole);
    String concurrency = qMatcher.group(3);
    return concurrency!=null?Integer.valueOf(concurrency):1;    
  }
  
}
