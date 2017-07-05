
package com.creactiviti.piper.core.messenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * a simple, non-thread-safe implementation of the 
 * {@link Messenger} interface. Useful for testing.
 * 
 * @author Arik Cohen
 * @since Jul 10, 2016
 */
public class SynchMessenger implements Messenger {

  private Map<String, List<Receiver>> listeners = new HashMap<> ();

  @Override
  public void send (String aRoutingKey, Object aMessage) {
    List<Receiver> list = listeners.get(aRoutingKey);
    Assert.isTrue(list!=null&&list.size()>0,"no listeners subscribed for: " + aRoutingKey);
    for(Receiver receiver : list) {
      receiver.receive(aMessage);
    }
  }

  public void receive (String aRoutingKey, Receiver aReceiver) {
    List<Receiver> list = listeners.get(aRoutingKey);
    if(list == null) {
      list = new ArrayList<>();
      listeners.put(aRoutingKey, list);
    }
    list.add(aReceiver);
  }
  
  public static interface Receiver {
    
    void receive (Object aMessage);
    
  }

}
