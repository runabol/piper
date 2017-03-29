package com.creactiviti.piper.core.pipeline;

import org.springframework.core.io.ByteArrayResource;

public class GitResource extends ByteArrayResource {

  private final String id;
  
  public GitResource(String aId, byte[] aData) {
    super(aData);
    id = aId;
  }

  public String getId() {
    return id;
  }
  
}
