/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.pipeline;

import java.util.List;

import com.creactiviti.piper.core.Accessor;
import com.creactiviti.piper.core.task.Task;

/**
 * Pipelines are the the blueprints that describe
 * the execution of a job.
 * 
 * @author Arik Cohen
 * @since Jun 12, 2016
 */
public interface Pipeline {

  /**
   * Returns the unique identifier of the pipeline. 
   */
  String getId ();

  /**
   * Returns a descriptive name for the pipeline. 
   */
  String getName ();
  
  /**
   * Returns the tasks that make up the pipeline.
   */
  List<Task> getTasks ();

  /**
   * Returns the pipeline's expected input
   */
  List<Accessor> getInput ();
}
