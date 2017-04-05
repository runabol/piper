package com.creactiviti.piper.core.messenger;

import org.springframework.amqp.core.AmqpTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

public class AmqpMessenger implements Messenger {
  
  private AmqpTemplate amqpTemplate;
  
  private static final String DEFAULT_QUEUE = "tasks";

  private ObjectMapper objectMapper = new ObjectMapper(); 
    
  @Override
  public void send (String aRoutingKey, Object aMessage) {
    try {
      amqpTemplate.convertAndSend(aRoutingKey!=null?aRoutingKey:DEFAULT_QUEUE, objectMapper.writeValueAsString(aMessage));
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public void setAmqpTemplate(AmqpTemplate aAmqpTemplate) {
    amqpTemplate = aAmqpTemplate;
  }
  
  public void setObjectMapper(ObjectMapper aObjectMapper) {
    objectMapper = aObjectMapper;
  }
}
