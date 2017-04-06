package com.creactiviti.piper.core.messenger;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.Assert;

public class AmqpMessenger implements Messenger {

  private AmqpTemplate amqpTemplate;

  @Override
  public void send (String aRoutingKey, Object aMessage) {
    Assert.notNull(aRoutingKey,"routing key can't be null");
    amqpTemplate.convertAndSend(aRoutingKey,aMessage);
  }

  public void setAmqpTemplate(AmqpTemplate aAmqpTemplate) {
    amqpTemplate = aAmqpTemplate;
  }

}
