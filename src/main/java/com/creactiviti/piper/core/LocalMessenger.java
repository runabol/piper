package com.creactiviti.piper.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="piper.messenger.provider",havingValue="local")
public class LocalMessenger implements Messenger {

  @Override
  public void send (String aRoutingKey, Object aMessage) {
  }

}
