/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.creactiviti.piper.core.annotations.ConditionalOnCoordinator;
import com.creactiviti.piper.core.context.Context;
import com.creactiviti.piper.core.context.ContextRepository;

@RestController
@ConditionalOnCoordinator
public class ContextsController {

  @Autowired private ContextRepository contextRepository;
  
  @GetMapping(value="/contexts/stack/{id}")
  public List<Context> stack (@PathVariable("id")String aContextObjectId) {
    return contextRepository.getStack(aContextObjectId);
  }
    
}
