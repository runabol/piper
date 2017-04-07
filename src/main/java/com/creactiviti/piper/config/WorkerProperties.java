package com.creactiviti.piper.config;

import java.util.HashMap;
import java.util.Map;

public class WorkerProperties {

  private boolean enabled = false;
  private Map<String,Object> subscriptions = new HashMap<>();
  
  public boolean isEnabled() {
    return enabled;
  }
  
  public void setEnabled(boolean aEnabled) {
    enabled = aEnabled;
  }

  public Map<String, Object> getSubscriptions() {
    return subscriptions;
  }
  
  public void setSubscriptions(Map<String, Object> aSubscriptions) {
    subscriptions = aSubscriptions;
  }
  
}
