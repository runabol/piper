/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

/**
 * A startegy interface used for executing a {@link JobTask}.
 * 
 * @author Arik Cohen
 * @since Jun 12, 2016
 */
public interface TaskHandler<O> {

  O handle (JobTask aTask) throws Exception;
  
}
