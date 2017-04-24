/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;


/**
 * The strategey interface used for resolving the 
 * apprpriate {@link TaskDispatcher} instance for a 
 * given {@link JobTask}.
 * 
 * @author Arik Cohen
 * @since Mar 26, 2017
 */
public interface TaskDispatcherResolver {
  
  /**
   * Resolves a {@link TaskDispatcher} for the given
   * {@link JobTask} instance or <code>null</code>
   * if one can not be resolved. 
   * 
   * @param aTask
   *           The {@link JobTask} instance
   * @return a {@link TaskDispatcher} instance to execute the given task or <code>null</code> if 
   *         unable to resolve one. 
   */
  TaskDispatcher resolve (Task aTask);
  
}
