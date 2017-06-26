/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
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
