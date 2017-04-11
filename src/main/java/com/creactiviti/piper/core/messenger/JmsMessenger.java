/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.messenger;

import java.util.concurrent.TimeUnit;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.Assert;

import com.creactiviti.piper.error.Retryable;

public class JmsMessenger implements Messenger {

  private JmsTemplate jmsTemplate;

  @Override
  public void send (String aRoutingKey, Object aMessage) {
    Assert.notNull(aRoutingKey,"routing key can't be null");
    if(aMessage instanceof Retryable) {
      Retryable r = (Retryable) aMessage;
      delay(r.getRetryDelay());
    }
    jmsTemplate.convertAndSend(aRoutingKey, aMessage);
  }
  
  private void delay (int aValue) {
    try {
      TimeUnit.MILLISECONDS.sleep(aValue);
    } catch (InterruptedException e) {
    }
  }

  public void setJmsTemplate(JmsTemplate aJmsTemplate) {
    jmsTemplate = aJmsTemplate;
  }

}
