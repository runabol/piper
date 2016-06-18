package com.creactiviti.piper.core;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class YamlPipelineRepository implements PipelineRepository  {

  @Override
  public List<Pipeline> findAll() {
    return null;
  }

  @Override
  public Pipeline find(String aId) {
    return null;
  }

}
