
package com.creactiviti.piper.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.creactiviti.piper.core.task.CancelTask;
import com.creactiviti.piper.core.task.SimpleTaskExecution;
import com.creactiviti.piper.core.task.TaskDispatcher;
import com.creactiviti.piper.core.task.TaskExecution;
import com.creactiviti.piper.core.task.TaskExecutionRepository;
import com.creactiviti.piper.core.task.TaskStatus;

/**
 *  
 * @author Arik Cohen
 * @since Apt 9, 2017
 */
public class TaskStartedEventListener implements EventListener {

  private final TaskExecutionRepository taskExecutionRepository;
  private final TaskDispatcher taskDispatcher;
  
  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  public TaskStartedEventListener (TaskExecutionRepository aTaskExecutionRepository, TaskDispatcher aTaskDispatcher) {
    taskExecutionRepository = aTaskExecutionRepository;
    taskDispatcher = aTaskDispatcher;
  }

  @Override
  public void onApplicationEvent (PiperEvent aEvent) {
    if(Events.TASK_STARTED.equals(aEvent.getType())) {
      String taskId = aEvent.getString("taskId");
      TaskExecution task = taskExecutionRepository.findOne(taskId);
      if(task == null) {
        logger.error("Unkown task: {}",taskId);
      }
      else if (task.getStatus() == TaskStatus.CANCELLED) {
        taskDispatcher.dispatch(new CancelTask(task.getId()));
      }
      else {
        SimpleTaskExecution mtask = SimpleTaskExecution.createForUpdate(task);
        if(mtask.getStartTime()==null && mtask.getStatus() != TaskStatus.STARTED) {
          mtask.setStartTime(aEvent.getCreateTime());
          mtask.setStatus(TaskStatus.STARTED);
          taskExecutionRepository.merge(mtask);
        }
        if(mtask.getParentId()!=null) {
          PiperEvent pevent = PiperEvent.of(Events.TASK_STARTED,"taskId",mtask.getParentId());
          onApplicationEvent(pevent);
        }
      }
    }
  }

}
