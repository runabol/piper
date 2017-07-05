
package com.creactiviti.piper.core.task;

import com.creactiviti.piper.core.Accessor;

public interface Task extends Accessor {
  
  /**
   * Get the type of the task. Type strings
   * are mapped to {@link TaskHandler} implementations
   * designed to handler that type of task. 
   * 
   * @return String
   */
  String getType ();
  
}
