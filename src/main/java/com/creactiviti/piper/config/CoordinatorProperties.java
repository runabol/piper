/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.config;

public class CoordinatorProperties {

  private boolean enabled = false;
  private CoordinatorSubscriptions subscriptions = new CoordinatorSubscriptions();
  
  public boolean isEnabled() {
    return enabled;
  }
  
  public void setEnabled(boolean aEnabled) {
    enabled = aEnabled;
  }
  
  public CoordinatorSubscriptions getSubscriptions() {
    return subscriptions;
  }
  
  public void setSubscriptions(CoordinatorSubscriptions aSubscriptions) {
    subscriptions = aSubscriptions;
  }
  
}
