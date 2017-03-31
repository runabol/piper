/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

public class PipelineRepository {

  private Git git;
  
  public Git getGit() {
    return git;
  }
  
  public void setGit(Git aGit) {
    git = aGit;
  }
  
}
