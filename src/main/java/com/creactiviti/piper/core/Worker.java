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
 * {@link Messager} interface.</p>
 * 
 * @author Arik Cohen
 * @since Jun 12, 2016
 *
 */
public interface Worker {

  /**
   * Start the execution of a {@link Task} asynchronously. 
   * 
   * @param aTask
   *          The task to execute.
   */
  void start (Task aTask);
  
  /**
   * Stop the execution of a running task.
   * 
   * @param aTaskId
   */
  void stop (String aTaskId);
  
}
