/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

public class GitProperties {

  private boolean enabled = false;
  private String url;
  private String[] searchPaths;
  
  public String getUrl() {
    return url;
  }
  
  public void setUrl(String aUrl) {
    url = aUrl;
  }

  public String[] getSearchPaths() {
    return searchPaths;
  }
  
  public void setSearchPaths(String[] aSearchPaths) {
    searchPaths = aSearchPaths;
  }
  
  public boolean isEnabled() {
    return enabled;
  }
  
  public void setEnabled(boolean aEnabled) {
    enabled = aEnabled;
  }
  
}
