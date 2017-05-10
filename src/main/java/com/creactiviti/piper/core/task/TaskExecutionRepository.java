/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
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
