/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.metrics;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.config.ConditionalOnCoordinator;
import com.creactiviti.piper.core.job.JobRepository;


/**
 * a {@link PublicMetrics} implementaion which calculates 
 * the number of jobs completed today.
 * 
 * @author Arik Cohen
 * @since Apr 8, 2017
 */
@Component
@ConditionalOnCoordinator 
public class JobsCompletedStatsContributor implements PublicMetrics {

  @Autowired
  private JobRepository jobRepository;
  
  @Override
  public Collection<Metric<?>> metrics() {
    return Arrays.asList(
        new Metric<>("jobs.completed.today", jobRepository.countCompletedJobsToday()),
        new Metric<>("jobs.completed.yesterday", jobRepository.countCompletedJobsYesterday())
    );
  }

}
