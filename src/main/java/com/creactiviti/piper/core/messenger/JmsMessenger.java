/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.messenger;

import org.springframework.jms.core.JmsTemplate;

import com.google.common.base.Throwables;


public class JmsMessenger implements Messenger {

  private JmsTemplate jmsTemplate;
  
  private static final String DEFAULT_QUEUE = "tasks";

  @Override
  public void send (String aRoutingKey, Object aMessage) {
    try {
      jmsTemplate.convertAndSend(aRoutingKey!=null?aRoutingKey:DEFAULT_QUEUE, aMessage);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
  
  public void setJmsTemplate(JmsTemplate aJmsTemplate) {
    jmsTemplate = aJmsTemplate;
  }
  
}
