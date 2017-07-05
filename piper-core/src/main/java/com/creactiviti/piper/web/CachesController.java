/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creactiviti.piper.core.annotations.ConditionalOnCoordinator;
import com.creactiviti.piper.core.cache.Clearable;

@RestController
@ConditionalOnCoordinator
public class CachesController {

  @Autowired(required=false)
  private List<Clearable> clearables = Collections.emptyList();
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @PostMapping("/caches/clear")
  public Map<String, String> clear () {
    for(Clearable c : clearables) {
      logger.info("Clearing: {}",c.getClass().getName());
      c.clear();
    }
    return Collections.singletonMap("status", "OK");
  }
}
