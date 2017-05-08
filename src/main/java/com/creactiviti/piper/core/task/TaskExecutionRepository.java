/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.List;

public interface TaskExecutionRepository {
  
  TaskExecution findOne (String aId);
  
  void create (TaskExecution aTaskExecution);
  
  void update (TaskExecution aTaskExecution);
  
  List<TaskExecution> getExecution (String aJobId);

  long completeSubTask (TaskExecution aTaskExecution); 
}
