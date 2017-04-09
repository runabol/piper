/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.config.ConditionalOnCoordinator;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.stats.Stats.Builder;


/**
 * a {@link StatsContributor} implementaion which calculates 
 * the number of jobs completed today.
 * 
 * @author Arik Cohen
 * @since Apr 8, 2017
 */
@Component
@ConditionalOnCoordinator 
public class JobsCompletedStatsContributor implements StatsContributor {

  @Autowired
  private JobRepository jobRepository;
  
  @Override
  public void contribute(Builder aBuilder) {
    aBuilder.withDetail("jobs.completed.today", jobRepository.countCompletedJobsToday());
    aBuilder.withDetail("jobs.completed.yesterday", jobRepository.countCompletedJobsYesterday());
  }

}
