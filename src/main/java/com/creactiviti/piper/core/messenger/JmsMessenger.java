/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.messenger;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.Assert;

import com.google.common.base.Throwables;


public class JmsMessenger implements Messenger {

  private JmsTemplate jmsTemplate;
  
  @Override
  public void send (String aRoutingKey, Object aMessage) {
    try {
      Assert.notNull(aRoutingKey,"routing key can't be null");
      jmsTemplate.convertAndSend(aRoutingKey, aMessage);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
  
  public void setJmsTemplate(JmsTemplate aJmsTemplate) {
    jmsTemplate = aJmsTemplate;
  }
  
}
