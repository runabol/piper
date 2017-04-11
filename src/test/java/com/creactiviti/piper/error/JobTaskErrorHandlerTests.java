package com.creactiviti.piper.error;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.job.MutableJob;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.task.JobTaskRepository;
import com.creactiviti.piper.core.task.TaskExecutor;

public class JobTaskErrorHandlerTests {

  private JobRepository jobRepo = mock(JobRepository.class);
  private JobTaskRepository taskRepo = mock(JobTaskRepository.class);
  private TaskExecutor executor = mock(TaskExecutor.class);
  
  @Test
  public void test1 () {
    when(jobRepo.findJobByTaskId("1234")).thenReturn(new MutableJob());
    JobTaskErrorHandler handler = new JobTaskErrorHandler();
    handler.setJobRepository(jobRepo);
    handler.setJobTaskRepository(taskRepo);
    MutableJobTask errorable = new MutableJobTask();
    errorable.setId("1234");
    errorable.setError(new ErrorObject("something bad happened", new String[0]));
    handler.handle(errorable);
    verify(jobRepo,times(1)).update(any());
    verify(taskRepo,times(1)).update(any());
  }
  
  @Test
  public void test2 () {
    when(jobRepo.findJobByTaskId("1234")).thenReturn(new MutableJob());
    JobTaskErrorHandler handler = new JobTaskErrorHandler();
    handler.setJobRepository(jobRepo);
    handler.setJobTaskRepository(taskRepo);
    handler.setTaskExecutor(executor);
    MutableJobTask errorable = new MutableJobTask();
    errorable.setId("1234");
    errorable.setRetry(1);
    errorable.setError(new ErrorObject("something bad happened", new String[0]));
    handler.handle(errorable);
    verify(jobRepo,never()).update(any());
    verify(taskRepo,times(1)).update(any());
    verify(executor,times(1)).execute(any());
  }
 
  
}
