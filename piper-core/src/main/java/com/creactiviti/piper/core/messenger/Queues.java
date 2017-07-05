
package com.creactiviti.piper.core.messenger;

import com.creactiviti.piper.core.uuid.UUIDGenerator;

public interface Queues {

  static final String COMPLETIONS = "completions";
  static final String ERRORS      = "errors";
  static final String JOBS        = "jobs";
  static final String EXECUTE     = "execute";
  static final String DLQ         = "dlq";
  static final String CONTROL     = "x.control." + UUIDGenerator.generate();
  static final String TASKS       = "tasks";
  static final String EVENTS      = "events";
  
}
