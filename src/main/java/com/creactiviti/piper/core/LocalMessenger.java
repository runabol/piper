package com.creactiviti.piper.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="piper.messenger.provider",havingValue="local")
public class LocalMessenger implements Messenger {

  private final Map<String, MessageListener> receivers = new HashMap<> ();
  
  @Override
  public void send (String aRoutingKey, Object aMessage) {
    
  }

  @Override
  public void receive (String aRoutingKey, MessageListener aMessageListener) {
    receivers.put(aRoutingKey, aMessageListener);
  }

}
