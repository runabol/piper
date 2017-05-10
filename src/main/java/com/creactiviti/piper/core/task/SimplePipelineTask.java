/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Map;

import com.creactiviti.piper.core.MapObject;


public class SimplePipelineTask extends MapObject implements PipelineTask {

  public SimplePipelineTask (Task aSource) {
    super(aSource.asMap());
  }
  
  public SimplePipelineTask (Map<String, Object> aSource) {
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
  
  public void setNode (String aNode) {
    set("node", aNode);
  }
  
  @Override
  public int getTaskNumber() {
    return getInteger("taskNumber",-1);
  }
  
  public void setTaskNumber (int aTaskNumber) {
    set("taskNumber", aTaskNumber);
  }
  
  @Override
  public String getTimeout() {
    return getString("timeout");
  }
  
}