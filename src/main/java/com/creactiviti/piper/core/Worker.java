/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.event.Events;
import com.creactiviti.piper.core.event.PiperEvent;
import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.messenger.Queues;
import com.creactiviti.piper.core.task.ControlTask;
import com.creactiviti.piper.core.task.TaskExecution;
import com.creactiviti.piper.core.task.TaskHandler;
import com.creactiviti.piper.core.task.TaskHandlerResolver;
import com.creactiviti.piper.core.task.TaskStatus;
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
  private final ExecutorService executors = Executors.newCachedThreadPool();
  private final Map<String, Future<?>> taskExecutions = new ConcurrentHashMap<>();

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private static final long DEFAULT_TIME_OUT = 24 * 60 * 60 * 1000; 
  
  /**
   * Handle the execution of a {@link TaskExecution}. Implementors
   * are expected to execute the task asynchronously. 
   * 
   * @param aTask
   *          The task to execute.
   */
  public void handle (TaskExecution aTask) {
    Future<?> future = executors.submit(() -> {
      try {
        logger.debug("Recived task: {}",aTask);
        TaskHandler<?> taskHandler = taskHandlerResolver.resolve(aTask);
        messenger.send(Queues.EVENTS, PiperEvent.of(Events.TASK_STARTED,"taskId",aTask.getId()));
        Object output = taskHandler.handle(aTask);
        SimpleTaskExecution completion = SimpleTaskExecution.createForUpdate(aTask);
        if(output!=null) {
          completion.setOutput(output);
        }
        completion.setStatus(TaskStatus.COMPLETED);
        completion.setEndTime(new Date());
        messenger.send(Queues.COMPLETIONS, completion);
      }
      catch (InterruptedException e) {
        // ignore
      }
      catch (Exception e) {
        handleException(aTask, e);
      }
    });
    
    taskExecutions.put(aTask.getId(), future);
    
    try {
      future.get(calculateTimeout(aTask), TimeUnit.MILLISECONDS);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      handleException(aTask, e);
    }
    catch (CancellationException e) {
      logger.debug("Cancelled task: {}", aTask.getId());
    }
    
  }
  
  private void handleException (TaskExecution aTask, Exception aException) {
    logger.error(aException.getMessage(),aException);
    SimpleTaskExecution task = SimpleTaskExecution.createForUpdate(aTask);
    task.setError(new ErrorObject(aException.getMessage(),ExceptionUtils.getStackFrames(aException)));
    task.setStatus(TaskStatus.FAILED);
    messenger.send(Queues.ERRORS, task);
  }
  
  private long calculateTimeout (TaskExecution aTask) {
    if(aTask.getTimeout() != null) {
      return Duration.parse("PT"+aTask.getTimeout()).toMillis();
    }
    return DEFAULT_TIME_OUT;
  }
  
  /**
   * Cancel the execution of a running task.
   * 
   * @param aTaskId
   *          The ID of the task to cancel.
   */
  public void handle (ControlTask aControlTask) {
    Assert.notNull(aControlTask,"task must not be null");
    if(ControlTask.TYPE_CANCEL.equals(aControlTask.getType())) {
      String taskId = aControlTask.getRequiredString("taskId");
      Future<?> future = taskExecutions.get(taskId);
      if(future != null) {
        logger.info("Cancelling task {}",taskId);
        future.cancel(true);
      }
    }
  }

  public void setTaskHandlerResolver(TaskHandlerResolver aTaskHandlerResolver) {
    taskHandlerResolver = aTaskHandlerResolver;
  }

  public void setMessenger(Messenger aMessenger) {
    messenger = aMessenger;
  }

}
