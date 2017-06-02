/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, May 2017
 */
package com.creactiviti.piper.core;

/**
 * 
 * @author Arik Cohen
 * @since May 26, 2017
 */
public class DSL {

  public static final String ID = "id";
  
  public static final String PARENT_ID = "parentId";
  
  public static final String JOB_ID = "jobId";
  
  public static final String LABEL = "label";
  
  public static final String NODE = "node";
  
  public static final String TYPE = "type";
  
  public static final String NAME = "name";
  
  public static final String RETRY = "retry";
  
  public static final String RETRY_ATTEMPTS = "retryAttempts";
  
  public static final String RETRY_DELAY = "retryDelay";
  
  public static final String RETRY_DELAY_FACTOR = "retryDelayFactor";
  
  public static final String EXECUTION_TIME = "executionTime";
  
  public static final String CREATE_TIME = "createTime";
  
  public static final String PIPELINE_ID = "pipelineId";
  
  public static final String START_TIME = "startTime";
  
  public static final String TASK_NUMBER = "taskNumber";
  
  public static final String TIMEOUT = "timeout";
  
  public static final String END_TIME = "endTime";
  
  public static final String STATUS = "status";
  
  public static final String OUTPUT = "output";
  
  public static final String ERROR = "error";
  
  public static final String TASKS = "tasks";
  
  public static final String INPUTS = "inputs";
  
  public static final String INPUT = "input";
  
  public static final String EXECUTION = "execution";
  
  public static final String TAGS = "tags";
  
  public static final String[] RESERVED_WORDS = new String[]{
                                                              ID,
                                                              PARENT_ID,
                                                              JOB_ID,
                                                              RETRY_ATTEMPTS,
                                                              EXECUTION_TIME,
                                                              CREATE_TIME,
                                                              START_TIME,
                                                              TASK_NUMBER,
                                                              END_TIME,
                                                              STATUS,
                                                              ERROR,
                                                              INPUT,
                                                              OUTPUT,
                                                              EXECUTION
                                                            }; 
  
}
