package com.creactiviti.piper.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;


public class HornetQMessenger implements Messenger {

  @Autowired
  private JmsTemplate jmsTemplate;

  @Override
  public void send (String aRoutingKey, Object aMessage) {
    jmsTemplate.convertAndSend("tasks", aMessage);
  }
  
  
}
