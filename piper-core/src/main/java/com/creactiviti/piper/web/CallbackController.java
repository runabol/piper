
package com.creactiviti.piper.web;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
public class CallbackController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @PostMapping(value="/callback")
  public Map<String, String> callback (@RequestBody Map<String,Object> aRequest) throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper ();
    om.enable(SerializationFeature.INDENT_OUTPUT);
    logger.info("{}",om.writeValueAsString(aRequest));
    return Collections.singletonMap("status", "OK");
  }
    
}
