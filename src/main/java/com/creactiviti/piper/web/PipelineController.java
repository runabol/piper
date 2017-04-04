/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.creactiviti.piper.config.ConditionalOnPredicate;
import com.creactiviti.piper.config.OnCoordinatorPredicate;
import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.pipeline.PipelineRepository;

@RestController
@ConditionalOnPredicate(OnCoordinatorPredicate.class)
public class PipelineController {

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
