package com.creactiviti.piper.core;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.creactiviti.piper.core.task.CounterRepository;
import com.creactiviti.piper.core.task.TaskExecution;
import com.creactiviti.piper.core.task.TaskExecutionRepository;
import com.creactiviti.piper.core.task.TaskStatus;

/**
 * @author Arik Cohen
 * @since June 4, 2017
 */
public class MapTaskCompletionHandler implements TaskCompletionHandler {

  private final TaskExecutionRepository taskExecutionRepo;
  private final TaskCompletionHandler taskCompletionHandler;
  private final CounterRepository counterRepository;
  
  public MapTaskCompletionHandler(TaskExecutionRepository aTaskExecutionRepo, TaskCompletionHandler aTaskCompletionHandler, CounterRepository aCounterRepository) {
    taskExecutionRepo = aTaskExecutionRepo;
    taskCompletionHandler = aTaskCompletionHandler;
    counterRepository = aCounterRepository;
  }
  
  @Override
  public void handle (TaskExecution aTaskExecution) {
    SimpleTaskExecution mtask = SimpleTaskExecution.createForUpdate(aTaskExecution);
    mtask.setStatus(TaskStatus.COMPLETED);
    taskExecutionRepo.merge(mtask);
    long subtasksLeft = counterRepository.decrement(aTaskExecution.getParentId());
    if(subtasksLeft == 0) {
      List<TaskExecution> children = taskExecutionRepo.findByParentId(aTaskExecution.getParentId());
      SimpleTaskExecution parentExecution = SimpleTaskExecution.createForUpdate(taskExecutionRepo.findOne(aTaskExecution.getParentId()));
      parentExecution.setEndTime(new Date ());
      parentExecution.setExecutionTime(parentExecution.getEndTime().getTime()-parentExecution.getStartTime().getTime());
      parentExecution.setOutput(children.stream().map(c->c.getOutput()).collect(Collectors.toList()));
      taskCompletionHandler.handle(parentExecution);
      counterRepository.delete(aTaskExecution.getParentId());
    }
  }

  @Override
  public boolean canHandle (TaskExecution aTaskExecution) {
    String parentId = aTaskExecution.getParentId();
    if(parentId!=null) {
      TaskExecution parentExecution = taskExecutionRepo.findOne(parentId);
      return parentExecution.getType().equals(DSL.MAP);
    }
    return false;
  }

}
