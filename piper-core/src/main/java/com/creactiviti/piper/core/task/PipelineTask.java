
package com.creactiviti.piper.core.task;

public interface PipelineTask extends Task {

  /**
   * Get the numeric order of the task
   * in the pipeline. 
   * 
   * @return int
   */
  int getTaskNumber ();
  
  /**
   * Get the identifier name of the task.
   * Task names are used for assigning the 
   * output of one task so it can be later
   * used by subsequent tasks.
   * 
   * @return String
   */
  String getName ();
  
  /**
   * Get the human-readable description 
   * of the task.
   * 
   * @return String
   */
  String getLabel ();
  
  /**
   * Defines the name of the type of 
   * node that the task execution will 
   * be routed to. So for instance if the 
   * node value is: "encoder", then the task
   * will be routed to the "encoder" queue
   * which is presumably subscribed to 
   * by worker nodes of "encoder" type.
   * 
   * @return String
   */
  String getNode ();
  
  /**
   * Returns the timeout expression which describes when this task
   * should be deemed as timed-out.
   * 
   * The formats accepted are based on the ISO-8601 
   * duration format with days considered to be exactly 24 hours.
   * 
   * @return String
   */
  String getTimeout ();
  
}
