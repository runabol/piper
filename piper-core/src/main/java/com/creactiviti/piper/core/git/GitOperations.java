/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.git;

import java.util.List;

import com.creactiviti.piper.core.pipeline.IdentifiableResource;

public interface GitOperations {
  
  /**
   * Returns the list of HEAD files within a Git repository. 
   * 
   * @param aUrl
   *          The Git Repository URL
   * @param aSearchPath
   *          The path to limit the search to.
   * @return the list of {@link IdentifiableResource}s within the given 
   * search path of the repository
   */
  List<IdentifiableResource> getHeadFiles (String aUrl, String... aSearchPath);
  
  /**
   * 
   */
  IdentifiableResource getFile (String aUrl, String aFileId);

}
