/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

/**
 * a strategy interface used for resolving a 
 * {@link TaskHandler} implementation which can handle
 * the given {@link TaskExecution} instance. Implementations 
 * are expected to return <code>null</code> if unable 
 * to resolve an appropriate {@link TaskHandler} implementation
 * to allow for chaining multiple {@link TaskHandlerResolver} 
 * implementations.
 * 
 * @author Arik Cohen
 * @since Jan 28, 2017
 */
public interface TaskHandlerResolver {

  TaskHandler<?> resolve (Task aJobTask);
  
}
