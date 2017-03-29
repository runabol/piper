package com.creactiviti.piper.git;

import java.util.List;

import com.creactiviti.piper.core.pipeline.GitResource;

public interface GitOperations {
  
  /**
   * Returns the list of HEAD files within a Git repository. 
   * 
   * @param aUrl
   *          The Git Repository URL
   * @param aSearchPath
   *          The path to limit the search to.
   * @return the list of {@link GitResource}s within the given 
   * search path of the repository
   */
  List<GitResource> getHeadFiles (String aUrl, String... aSearchPath);

}
