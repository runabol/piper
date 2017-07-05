
package com.creactiviti.piper.core.task;

/**
 * A strategy interface for dispatching {@link Task}
 * instances to be executed.
 * 
 * @author Arik Cohen
 * @since Mar 26, 2017
 */
public interface TaskDispatcher<T extends Task> {

  /**
   * Dispatches a {@link Task} instance.
   * 
   * @param aTask
   *          The task to dispatch
   */
  void dispatch (T aTask);

}
