/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.creactiviti.piper.core.event.Events;
import com.creactiviti.piper.core.event.PiperEvent;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.messenger.Queues;
import com.creactiviti.piper.core.task.ControlTask;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.TaskHandler;
import com.creactiviti.piper.core.task.TaskHandlerResolver;
import com.creactiviti.piper.error.ErrorObject;

/**
 * <p>The class responsible for executing tasks spawned by the {@link Coordinator}.</p>
 * 
 * <p>Worker threads typically execute on a different
 * process than the {@link Coordinator} process and most likely
 * on a seperate node altogether.</p>
 * 
 * <p>Communication between the two is decoupled through the 
 * {@link Messenger} interface.</p>
 * 
 * @author Arik Cohen
 * @since Jun 12, 2016
 *
 */
public class Worker {

  private TaskHandlerResolver taskHandlerResolver;
  private Messenger messenger;
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private final ExecutorService executors = Executors.newCachedThreadPool();

  /**
   * Handle the execution of a {@link JobTask}. Implementors
   * are expected to execute the task asynchronously. 
   * 
   * @param aTask
   *          The task to execute.
   */
  public void handle (JobTask aTask) {
    try {
      logger.debug("Recived task: {}",aTask);
      TaskHandler<?> taskHandler = taskHandlerResolver.resolve(aTask);
      messenger.send(Queues.EVENTS, PiperEvent.of(Events.TASK_STARTED,"taskId",aTask.getId()));
      Object output = taskHandler.handle(aTask);
      MutableJobTask completion = new MutableJobTask(aTask);
      if(output!=null) {
        completion.setOutput(output);
      }
      completion.setCompletionDate(new Date());
      messenger.send(Queues.COMPLETIONS, completion);
    }
    catch (Exception e) {
      logger.error(e.getMessage(),e);
      MutableJobTask jobTask = new MutableJobTask(aTask);
      jobTask.setError(new ErrorObject(e.getMessage(),ExceptionUtils.getStackFrames(e)));
      messenger.send(Queues.ERRORS, jobTask);
    }
  }
  
  /**
   * Cancel the execution of a running task.
   * 
   * @param aTaskId
   *          The ID of the task to cancel.
   */
  public void handle (ControlTask aControlTask) {
    throw new UnsupportedOperationException();
  }

  public void setTaskHandlerResolver(TaskHandlerResolver aTaskHandlerResolver) {
    taskHandlerResolver = aTaskHandlerResolver;
  }

  public void setMessenger(Messenger aMessenger) {
    messenger = aMessenger;
  }

}
