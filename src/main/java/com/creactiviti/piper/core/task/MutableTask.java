/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Map;

import com.creactiviti.piper.core.MapObject;


public class MutableTask extends MapObject implements Task {

  public MutableTask (Task aSource) {
    super(aSource.asMap());
  }
  
  public MutableTask (Map<String, Object> aSource) {
    super(aSource);
  }
  
  @Override
  public String getType() {
    return getString("type");
  }

  @Override
  public String getName() {
    return getString("name");
  }
  
  @Override
  public String getLabel() {
    return getString("label");
  }

  @Override
  public String getNode() {
    return getString("node");
  }
  
}