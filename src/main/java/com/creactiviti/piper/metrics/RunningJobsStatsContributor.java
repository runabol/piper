/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.metrics;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.config.ConditionalOnCoordinator;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.stats.StatsContributor;


/**
 * a {@link StatsContributor} implementaion which calculates 
 * the number of jobs currently running.
 * 
 * @author Arik Cohen
 * @since Apr 8, 2017
 */
@Component
@ConditionalOnCoordinator 
public class RunningJobsStatsContributor implements PublicMetrics {

  @Autowired
  private JobRepository jobRepository;
  
  @Override
  public Collection<Metric<?>> metrics() {
    return Collections.singletonList(new Metric("jobs.running", jobRepository.countRunningJobs()));
  }

}
