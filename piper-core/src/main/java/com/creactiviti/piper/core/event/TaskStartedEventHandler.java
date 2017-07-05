
package com.creactiviti.piper.core.event;

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
  
  public TaskStartedEventHandler (TaskExecutionRepository aJobTaskRepository, TaskDispatcher aTaskDispatcher) {
    jobTaskRepository = aJobTaskRepository;
    taskDispatcher = aTaskDispatcher;
  }

  @Override
  public void onApplicationEvent (PayloadApplicationEvent<PiperEvent> aEvent) {
    if(Events.TASK_STARTED.equals(aEvent.getPayload().getType())) {
      String taskId = aEvent.getPayload().getString("taskId");
      TaskExecution task = jobTaskRepository.findOne(taskId);
      if(task == null) {
        logger.error("Unkown task: {}",taskId);
      }
      else if (task.getStatus() == TaskStatus.CANCELLED) {
        taskDispatcher.dispatch(new CancelTask(task.getId()));
      }
      else {
        SimpleTaskExecution mtask = SimpleTaskExecution.createForUpdate(task);
        if(mtask.getStartTime()==null && mtask.getStatus() != TaskStatus.STARTED) {
          mtask.setStartTime(aEvent.getPayload().getCreateTime());
          mtask.setStatus(TaskStatus.STARTED);
          jobTaskRepository.merge(mtask);
        }
        if(mtask.getParentId()!=null) {
          PiperEvent pevent = PiperEvent.of(Events.TASK_STARTED,"taskId",mtask.getParentId());
          onApplicationEvent(new PayloadApplicationEvent<PiperEvent>(pevent, pevent));
        }
      }
    }
  }

}
