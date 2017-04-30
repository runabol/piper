/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.List;

public interface JobTaskRepository {
  
  JobTask findOne (String aJobTaskId);
  
  void create (JobTask aJobTask);
  
  void update (JobTask aJobTask);
  
  List<JobTask> getTasks (String aJobId);

  long completeSubTask (JobTask aJobSubTask); 
}
