/*
 * Copyright 2016-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.creactiviti.piper.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.pipeline.PipelineService;
import com.creactiviti.piper.core.pipeline.SimplePipeline;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.creactiviti.piper.core.annotations.ConditionalOnCoordinator;
import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.pipeline.PipelineRepository;

@RestController
@ConditionalOnCoordinator
public class PipelinesController {

  private final PipelineRepository pipelineRepository;
  private final PipelineService pipelineService;
  private final Coordinator coordinator;

  public PipelinesController(PipelineRepository pipelineRepository, PipelineService pipelineService, Coordinator coordinator) {
    this.pipelineRepository = pipelineRepository;
    this.pipelineService = pipelineService;
    this.coordinator = coordinator;
  }

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

  @PostMapping("/pipelines")
  public void create (@RequestBody Map<String, Object> aSource) {
    pipelineService.save(new SimplePipeline(aSource));
  }
  
}
