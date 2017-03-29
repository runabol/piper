package com.creactiviti.piper.core.pipeline;

import java.io.File;

import org.springframework.core.io.FileSystemResource;

public class GitResource extends FileSystemResource {

  private final String id;
  
  public GitResource(String aId, File aFile) {
    super(aFile);
    id = aId;
  }

  public String getId() {
    return id;
  }
  
}
