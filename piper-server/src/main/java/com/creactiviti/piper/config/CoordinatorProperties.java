
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
