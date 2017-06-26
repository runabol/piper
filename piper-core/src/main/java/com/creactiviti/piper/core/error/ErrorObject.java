/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.error;

import java.util.Map;

import com.creactiviti.piper.core.MapObject;

/**
 * 
 * @author Arik Cohen
 * @since Apr 10, 2017
 */
public class ErrorObject extends MapObject implements Error {

  public ErrorObject () {
  }
  
  public ErrorObject (Map<String,Object> aSource) {
    super(aSource);
  }
  
  public ErrorObject (String aMessage, String[] aStackTrace) {
    set("message", aMessage);
    set("stackTrace", aStackTrace);
  } 
  
  @Override
  public String getMessage() {
    return getString("message");
  }

  @Override
  public String[] getStackTrace() {
    return getArray("stackTrace", String.class);
  }

}
