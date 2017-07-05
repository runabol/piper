
package com.creactiviti.piper.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.creactiviti.piper.core.annotations.ConditionalOnCoordinator;
import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.pipeline.PipelineRepository;

@RestController
@ConditionalOnCoordinator
public class PipelinesController {

  @Autowired
  private PipelineRepository pipelineRepository;
  
  @GetMapping("/pipelines")
  public List<Pipeline> list () {
    return pipelineRepository.findAll();
  }
  
  @GetMapping("/pipelines/**")
  public Pipeline get (HttpServletRequest aRequest) {
    String path = (String) aRequest.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String pipelineId = path.replaceFirst("/pipelines/", "");
    return pipelineRepository.findOne(pipelineId);
  }
  
}
