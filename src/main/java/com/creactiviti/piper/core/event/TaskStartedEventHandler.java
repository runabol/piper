/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;

import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.task.CancelTask;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.JobTaskRepository;
import com.creactiviti.piper.core.task.TaskDispatcher;
import com.creactiviti.piper.core.task.TaskStatus;

/**
 * 
 * @author Arik Cohen
 * @since Apt 9, 2017
 */
public class TaskStartedEventHandler implements ApplicationListener<PayloadApplicationEvent<PiperEvent>> {

  private final JobTaskRepository jobTaskRepository;
  private final TaskDispatcher taskDispatcher;
  
  public TaskStartedEventHandler(JobTaskRepository aJobTaskRepository, TaskDispatcher aTaskDispatcher) {
    jobTaskRepository = aJobTaskRepository;
    taskDispatcher = aTaskDispatcher;
  }

  @Override
  public void onApplicationEvent(PayloadApplicationEvent<PiperEvent> aEvent) {
    PiperEvent event = aEvent.getPayload();
    if(Events.TASK_STARTED.equals(event.getType())) {
      String taskId = event.getString("taskId");
      JobTask task = jobTaskRepository.findOne(taskId);
      if(task.getStatus() == TaskStatus.CREATED) {
        MutableJobTask mtask = MutableJobTask.createForUpdate(task);
        mtask.setStatus(TaskStatus.STARTED);
        jobTaskRepository.update(mtask);
      }
      else if (task.getStatus() == TaskStatus.CANCELLED) {
        taskDispatcher.dispatch(new CancelTask(task.getId()));
      }
    }
  }

}
