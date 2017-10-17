
package com.creactiviti.piper.core.task;

import com.creactiviti.piper.core.DSL;
import com.creactiviti.piper.core.TaskCompletionHandler;

/**
 * <p>A {@link TaskCompletionHandler} implementation which handles completions 
 * of parallel construct tasks.</p>
 * 
 * <p>This handler keeps track of how many tasks were completed so far and 
 * when all parallel tasks completed for a given task it will then complete
 * the overall <code>parallel</code> task.</p> 
 * 
 * @author Arik Cohen
 * @since May 12, 2017
 * @see ParallelTaskDispatcher
 */
public class ParallelTaskCompletionHandler implements TaskCompletionHandler {

  private TaskExecutionRepository taskExecutionRepo;
  private TaskCompletionHandler taskCompletionHandler;
  private CounterRepository counterRepository;
  
  @Override
  public void handle (TaskExecution aTaskExecution) {
    SimpleTaskExecution mtask = SimpleTaskExecution.createForUpdate(aTaskExecution);
    mtask.setStatus(TaskStatus.COMPLETED);
    taskExecutionRepo.merge(mtask);
    long tasksLeft = counterRepository.decrement(aTaskExecution.getParentId());
    if(tasksLeft == 0) {
      taskCompletionHandler.handle(taskExecutionRepo.findOne(aTaskExecution.getParentId()));
      counterRepository.delete(aTaskExecution.getParentId());
    }
  }

  @Override
  public boolean canHandle (TaskExecution aTaskExecution) {
    String parentId = aTaskExecution.getParentId();
    if(parentId!=null) {
      TaskExecution parentExecution = taskExecutionRepo.findOne(parentId);
      return parentExecution.getType().equals(DSL.PARALLEL);
    }
    return false;
  }
  
  public void setTaskExecutionRepository(TaskExecutionRepository aTaskExecutionRepo) {
    taskExecutionRepo = aTaskExecutionRepo;
  }
  
  public void setTaskCompletionHandler(TaskCompletionHandler aTaskCompletionHandler) {
    taskCompletionHandler = aTaskCompletionHandler;
  }
  
  public void setCounterRepository(CounterRepository aCounterRepository) {
    counterRepository = aCounterRepository;
  }

}
