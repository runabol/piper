
package com.creactiviti.piper.core.task;

import java.util.Map;

import com.creactiviti.piper.core.DSL;
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
    return getString(DSL.TYPE);
  }
  
  @Override
  public String getName() {
    return getString(DSL.NAME);
  }
  
  @Override
  public String getLabel() {
    return getString(DSL.LABEL);
  }

  @Override
  public String getNode() {
    return getString(DSL.NODE);
  }
  
  public void setNode (String aNode) {
    set(DSL.NODE, aNode);
  }
  
  @Override
  public int getTaskNumber() {
    return getInteger(DSL.TASK_NUMBER,-1);
  }
  
  public void setTaskNumber (int aTaskNumber) {
    set(DSL.TASK_NUMBER, aTaskNumber);
  }
  
  @Override
  public String getTimeout() {
    return getString(DSL.TIMEOUT);
  }
  
}