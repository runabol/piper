/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.task;

/**
 * 
 * @author Arik Cohen
 * @since Apr 19, 2017
 */
public class CancelTask extends MutableControlTask {

  public CancelTask (String aTaskId) {
    super(ControlTask.TYPE_CANCEL);
    set("taskId", aTaskId);
  }
  
}
