/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.messenger;

import com.creactiviti.piper.core.uuid.UUIDGenerator;

public interface Queues {

  static final String COMPLETIONS = "completions";
  static final String ERRORS      = "errors";
  static final String EXECUTIONS  = "executions";
  static final String DLQ         = "dlq";
  static final String CONTROL     = "x.control." + UUIDGenerator.generate();
  static final String TASKS       = "tasks";
  static final String EVENTS      = "events";
  
}
