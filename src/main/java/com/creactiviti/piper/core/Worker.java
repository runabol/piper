package com.creactiviti.piper.core;

/**
 * <p>The interface to be implemented by object responsible for 
 * executing tasks spawned by the {@link Coordinator}.</p>
 * 
 * <p>Implementations will typically execute on a different
 * process than the {@link Coordinator} process and most likely
 * on a seperate node altogether.</p>
 * 
 * <p>Communication between the two is decoupled through the 
 * {@link Messenger} interface.</p>
 * 
 * @author Arik Cohen
 * @since Jun 12, 2016
 *
 */
public interface Worker {

  /**
   * Handle the execution of a {@link Task}. Implementors
   * are expected to execute the task asynchronously. 
   * 
   * @param aTask
   *          The task to execute.
   */
  void handle (Task aTask);
  
  /**
   * Cancel the execution of a running task.
   * 
   * @param aTaskId
   *          The ID of the task to cancel.
   */
  void cancel (String aTaskId);
  
}
