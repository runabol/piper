
package com.creactiviti.piper.core.task;

import java.util.List;

public interface TaskExecutionRepository {
  
  /**
   * Find a single {@link TaskExecution} instance by its id.
   * 
   * @param aId
   * @return TaskExecution
   */
  TaskExecution findOne (String aId);
  
  /**
   * Returns a collection of {@link TaskExecution} instances which 
   * are the children of the given parent id.
   * 
   * @param aParentId
   * @return
   */
  List<TaskExecution> findByParentId (String aParentId);

  /**
   * Creates a new persistent represenation of the given {@link TaskExecution}.
   * 
   * @param aTaskExecution
   */
  void create (TaskExecution aTaskExecution);

  /**
   * Merges the state of the given {@link TaskExecution} instance
   * with its persistent representation and returns the merged
   * version.
   * 
   * @param aTaskExecution
   */
  TaskExecution merge (TaskExecution aTaskExecution);
  
  /**
   * Returns the execution steps of the given job 
   * 
   * @param aJobId
   * @return List<TaskExecution>
   */
  List<TaskExecution> getExecution (String aJobId);

  
}
