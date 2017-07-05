
package com.creactiviti.piper.core.task;

/**
 * 
 * @author Arik Cohen
 * @since Apr 19, 2017
 */
public class CancelTask extends SimpleControlTask {

  public CancelTask () { }
  
  public CancelTask (String aTaskId) {
    super(ControlTask.TYPE_CANCEL);
    set("taskId", aTaskId);
  }
  
}
