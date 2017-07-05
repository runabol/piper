
package com.creactiviti.piper.core.task;

import com.creactiviti.piper.core.MapObject;

/**
 * 
 * @author Arik Cohen
 * @since Apr 11, 2017
 */
public class SimpleControlTask extends MapObject implements ControlTask {

  public SimpleControlTask() {}
  
  public SimpleControlTask(String aType) {
    set("type", aType);
  }
  
  @Override
  public String getType() {
    return getString("type");
  }
  
  public void setType (String aType) {
    set("type", aType);
  }
  
}
