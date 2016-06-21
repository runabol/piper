package com.creactiviti.piper.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LocalMessenger implements Messenger {

  private final Map<String,List<MessageListener>> router = new HashMap<> ();
  
  @Override
  public synchronized void send (String aRoutingKey, Object aMessage) {
    
  }
  
  public synchronized void receive (String aRoutingKey, MessageListener aListener) {
    List<MessageListener> listeners = router.get(aRoutingKey);
    if(listeners == null) {
      listeners = new ArrayList<> ();
      router.put(aRoutingKey, listeners);
    }
    listeners.add(aListener);
  }
  
  public static interface MessageListener {
    void onMessage (Object aMessage);
  }
  
}
