
package com.creactiviti.piper.core.task;

/**
 * Defines the various states that a {@link TaskExecution}
 * can be in at any give moment in time.
 * 
 * @author Arik Cohen
 */
public enum TaskStatus {

  CREATED(false),
  STARTED(false), 
  FAILED(true), 
  CANCELLED(true), 
  COMPLETED(true);
  
  private final boolean terminated;
  
  TaskStatus (boolean aTerminated) {
    terminated = aTerminated;
  }
  
  public boolean isTerminated() {
    return terminated;
  }
}
