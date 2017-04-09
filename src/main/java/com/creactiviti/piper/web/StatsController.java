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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creactiviti.piper.config.ConditionalOnCoordinator;
import com.creactiviti.piper.stats.Stats;
import com.creactiviti.piper.stats.StatsContributor;

/**
 * Exposes application stats such as what jobs are currently 
 * running, what's in the work queues etc.
 * 
 * @author Arik Cohen
 * @since Apt 7, 2017
 */
@RestController
@ConditionalOnCoordinator
public class StatsController {

  private final List<StatsContributor> statsContributors;
  
  public StatsController () {
    this(Collections.EMPTY_LIST);
  }

  @Autowired(required=false)
  public StatsController (List<StatsContributor> aContributors) {
    statsContributors = aContributors;
  }

  @GetMapping("/stats")
  public Map<String, Object> render () {
    Stats.Builder builder = new Stats.Builder();
    for (StatsContributor contributor : statsContributors) {
      contributor.contribute(builder);
    }
    Stats build = builder.build();
    return build.getDetails();
  }
  
}
