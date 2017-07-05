
package com.creactiviti.piper.core.task;

/**
 * A startegy interface used for executing a {@link TaskExecution}.
 * 
 * @author Arik Cohen
 * @since Jun 12, 2016
 */
public interface TaskHandler<O> {

  O handle (Task aTask) throws Exception;
  
}
