package com.creactiviti.piper.core;

import com.creactiviti.piper.core.task.JobTask;

/**
 * a strategy interface used for resolving a 
 * {@link TaskHandler} implementation which can handle
 * the given {@link JobTask} instance. Implementations 
 * are expected to return <code>null</code> if unable 
 * to resolve an appropriate {@link TaskHandler} implementation
 * to allow for chaining multiple {@link TaskHandlerResolver} 
 * implementations.
 * 
 * @author Arik Cohen
 * @since Jan 28, 2017
 */
public interface TaskHandlerResolver {

  TaskHandler<?> resolve (JobTask aJobTask);
  
}
