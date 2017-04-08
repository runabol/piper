/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.stats;

/**
 * Contributes additional stats details.
 *
 * @author Arik Cohen
 * @since Apr 7, 2017
 */
public interface StatsContributor {

  /**
   * Contributes additional details using the specified {@link Stats.Builder Builder}.
   * @param aBuilder the builder to use
   */
  void contribute(Stats.Builder aBuilder);

}