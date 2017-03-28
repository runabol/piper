package com.creactiviti.piper.core.pipeline;

import org.springframework.core.io.DefaultResourceLoader;

public class CustomResourceLoader extends DefaultResourceLoader {

  public CustomResourceLoader () {
    addProtocolResolver(new GitProtocolResolver());
  }
  
}
