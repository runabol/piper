/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.event;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;

import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.creactiviti.piper.core.task.CancelTask;
import com.creactiviti.piper.core.task.TaskDispatcher;
import com.creactiviti.piper.core.task.TaskExecution;
import com.creactiviti.piper.core.task.TaskExecutionRepository;
import com.creactiviti.piper.core.task.TaskStatus;

/**
 * 
 * @author Arik Cohen
 * @since Apt 9, 2017
 */
public class TaskStartedEventHandler implements ApplicationListener<PayloadApplicationEvent<PiperEvent>> {

  private final TaskExecutionRepository jobTaskRepository;
  private final TaskDispatcher taskDispatcher;
  
  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  public TaskStartedEventHandler(TaskExecutionRepository aJobTaskRepository, TaskDispatcher aTaskDispatcher) {
    jobTaskRepository = aJobTaskRepository;
    taskDispatcher = aTaskDispatcher;
  }

  @Override
  public void onApplicationEvent(PayloadApplicationEvent<PiperEvent> aEvent) {
    PiperEvent event = aEvent.getPayload();
    if(Events.TASK_STARTED.equals(event.getType())) {
      String taskId = event.getString("taskId");
      TaskExecution task = jobTaskRepository.findOne(taskId);
      if(task == null) {
        logger.error("Unkown task: {}",taskId);
      }
      else if(task.getStatus() == TaskStatus.CREATED) {
        SimpleTaskExecution mtask = SimpleTaskExecution.createForUpdate(task);
        mtask.setStartTime(new Date());
        mtask.setStatus(TaskStatus.STARTED);
        jobTaskRepository.merge(mtask);
      }
      else if (task.getStatus() == TaskStatus.CANCELLED) {
        taskDispatcher.dispatch(new CancelTask(task.getId()));
      }
    }
  }

}
