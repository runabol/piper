package com.creactiviti.piper.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.pipeline.PipelineRepository;

@RestController
public class PipelineController {

  @Autowired
  private PipelineRepository pipelineRepository;
  
  @GetMapping("/pipelines")
  public List<Pipeline> list () {
    return pipelineRepository.findAll();
  }
  
}
