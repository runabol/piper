package com.creactiviti.piper.core;

import com.creactiviti.piper.core.task.TaskExecution;

/**
 * A strategy interface for handling {@link TaskExecution}
 * completions.
 * 
 * @author Arik Cohen
 * @since Apr 23, 2017
 */
public interface TaskCompletionHandler {
  
  void handle (TaskExecution aJobTask);
  
  boolean canHandle (TaskExecution aJobTask);

}
