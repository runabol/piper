
package com.creactiviti.piper.core.task;

import com.creactiviti.piper.core.messenger.Exchanges;
import com.creactiviti.piper.core.messenger.Messenger;

/**
 * 
 * @author Arik Cohen
 * @since Apr 11, 2017
 */
public class ControlTaskDispatcher implements TaskDispatcher<ControlTask>, TaskDispatcherResolver {

  private final Messenger messenger;
  
  public ControlTaskDispatcher (Messenger aMessenger) {
    messenger = aMessenger;
  }
  
  @Override
  public void dispatch(ControlTask aTask) {
    messenger.send(Exchanges.CONTROL+"/"+Exchanges.CONTROL, aTask);
  }

  @Override
  public TaskDispatcher resolve(Task aTask) {
    if(aTask instanceof ControlTask) {
      return this; 
    }
    return null;
  }

}
