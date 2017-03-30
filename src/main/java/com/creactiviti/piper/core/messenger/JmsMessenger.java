package com.creactiviti.piper.core.messenger;

import org.springframework.jms.core.JmsTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;


public class JmsMessenger implements Messenger {

  private JmsTemplate jmsTemplate;
  
  private static final String DEFAULT_QUEUE = "tasks";

  private ObjectMapper objectMapper = new ObjectMapper(); 
    
  @Override
  public void send (String aRoutingKey, Object aMessage) {
    try {
      jmsTemplate.convertAndSend(aRoutingKey!=null?aRoutingKey:DEFAULT_QUEUE, objectMapper.writeValueAsString(aMessage));
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
  
  public void setJmsTemplate(JmsTemplate aJmsTemplate) {
    jmsTemplate = aJmsTemplate;
  }
  
  public void setObjectMapper(ObjectMapper aObjectMapper) {
    objectMapper = aObjectMapper;
  }
  
  
}
