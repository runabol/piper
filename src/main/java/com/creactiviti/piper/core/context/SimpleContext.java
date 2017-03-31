/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.context;

import java.util.Map;

import com.creactiviti.piper.core.MapObject;

public class SimpleContext extends MapObject implements Context {

  public SimpleContext (Map<String, Object> aSource) {
    super(aSource);
  }
  
}
