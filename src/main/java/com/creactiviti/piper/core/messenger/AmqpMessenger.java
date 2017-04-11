/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.messenger;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.Assert;

public class AmqpMessenger implements Messenger {

  private AmqpTemplate amqpTemplate;

  @Override
  public void send (String aRoutingKey, Object aMessage) {
    Assert.notNull(aRoutingKey,"routing key can't be null");
    amqpTemplate.convertAndSend(Exchanges.DEFAULT,aRoutingKey,aMessage);
  }

  public void setAmqpTemplate(AmqpTemplate aAmqpTemplate) {
    amqpTemplate = aAmqpTemplate;
  }

}
