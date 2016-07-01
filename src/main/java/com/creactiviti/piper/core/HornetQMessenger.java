package com.creactiviti.piper.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;


public class HornetQMessenger implements Messenger {

  @Autowired
  private JmsTemplate jmsTemplate;
  
  private static final String DEFAULT_QUEUE = "tasks";

  @Override
  public void send (String aRoutingKey, Object aMessage) {
    jmsTemplate.convertAndSend(aRoutingKey!=null?aRoutingKey:DEFAULT_QUEUE, aMessage);
  }
  
  
}
