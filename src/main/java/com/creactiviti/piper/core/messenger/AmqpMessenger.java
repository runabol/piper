package com.creactiviti.piper.core.messenger;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.Assert;

import com.google.common.base.Throwables;

public class AmqpMessenger implements Messenger {
  
  private AmqpTemplate amqpTemplate;
  
  @Override
  public void send (String aRoutingKey, Object aMessage) {
    try {
      Assert.notNull(aRoutingKey,"routing key can't be null");
      amqpTemplate.convertAndSend(aRoutingKey,aMessage);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public void setAmqpTemplate(AmqpTemplate aAmqpTemplate) {
    amqpTemplate = aAmqpTemplate;
  }
  
}
