package com.creactiviti.piper.git;

import java.util.List;

import com.creactiviti.piper.core.pipeline.GitResource;

public interface GitOperations {
  
  List<GitResource> getHeadFiles (String aUrl, String aSearchPath);

}
