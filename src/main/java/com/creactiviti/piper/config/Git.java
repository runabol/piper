package com.creactiviti.piper.config;

public class Git {

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
  
}
