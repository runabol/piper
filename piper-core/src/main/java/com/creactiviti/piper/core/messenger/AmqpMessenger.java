/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.messenger;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.error.Prioritizable;
import com.creactiviti.piper.core.error.Retryable;

public class AmqpMessenger implements Messenger {

  private AmqpTemplate amqpTemplate;

  @Override
  public void send (String aRoutingKey, Object aMessage) {
    Assert.notNull(aRoutingKey,"routing key can't be null");
    amqpTemplate.convertAndSend(determineExchange(aRoutingKey),determineRoutingKey(aRoutingKey),aMessage, (m) -> {
      if(aMessage instanceof Retryable) {
        Retryable r = (Retryable) aMessage;
        m.getMessageProperties().setDelay((int)r.getRetryDelayMillis());
      }
      if(aMessage instanceof Prioritizable) {
        Prioritizable p = (Prioritizable) aMessage;
        m.getMessageProperties().setPriority(p.getPriority());
      }
      return m;
    });
  }

  private String determineExchange (String aRoutingKey) {
    String[] routingKey = aRoutingKey.split("/");
    Assert.isTrue(routingKey.length<=2,"Invalid routing key: " + aRoutingKey);
    return routingKey.length==2?routingKey[0]:Exchanges.TASKS; 
  }
  
  private String determineRoutingKey (String aRoutingKey) {
    String[] routingKey = aRoutingKey.split("/");
    Assert.isTrue(routingKey.length<=2,"Invalid routing key: " + aRoutingKey);
    return routingKey.length==2?routingKey[1]:aRoutingKey; 
  }
  
  public void setAmqpTemplate(AmqpTemplate aAmqpTemplate) {
    amqpTemplate = aAmqpTemplate;
  }

}
