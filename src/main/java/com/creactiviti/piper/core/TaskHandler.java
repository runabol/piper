package com.creactiviti.piper.core;

import com.creactiviti.piper.core.task.JobTask;

/**
 * A startegy interface used for executing a {@link JobTask}.
 * 
 * @author Arik Cohen
 * @since Jun 12, 2016
 */
public interface TaskHandler<O> {

  O handle (JobTask aTask);
  
}
