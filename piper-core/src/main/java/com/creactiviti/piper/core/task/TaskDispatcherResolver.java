
package com.creactiviti.piper.core.task;


/**
 * The strategey interface used for resolving the 
 * apprpriate {@link TaskDispatcher} instance for a 
 * given {@link TaskExecution}.
 * 
 * @author Arik Cohen
 * @since Mar 26, 2017
 */
public interface TaskDispatcherResolver {
  
  /**
   * Resolves a {@link TaskDispatcher} for the given
   * {@link TaskExecution} instance or <code>null</code>
   * if one can not be resolved. 
   * 
   * @param aTask
   *           The {@link TaskExecution} instance
   * @return a {@link TaskDispatcher} instance to execute the given task or <code>null</code> if 
   *         unable to resolve one. 
   */
  TaskDispatcher resolve (Task aTask);
  
}
