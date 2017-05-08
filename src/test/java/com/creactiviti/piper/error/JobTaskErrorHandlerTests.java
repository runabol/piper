package com.creactiviti.piper.error;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;

import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.job.MutableJob;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.task.TaskExecutionRepository;
import com.creactiviti.piper.core.task.TaskDispatcher;

public class JobTaskErrorHandlerTests {

  private JobRepository jobRepo = mock(JobRepository.class);
  private TaskExecutionRepository taskRepo = mock(TaskExecutionRepository.class);
  private TaskDispatcher taskDispatcher = mock(TaskDispatcher.class);
  private ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
  
  @Test
  public void test1 () {
    when(jobRepo.findJobByTaskId("1234")).thenReturn(new MutableJob(Collections.singletonMap("id","4567")));
    JobTaskErrorHandler handler = new JobTaskErrorHandler();
    handler.setEventPublisher(eventPublisher);
    handler.setJobRepository(jobRepo);
    handler.setJobTaskRepository(taskRepo);
    MutableJobTask errorable = MutableJobTask.create();
    errorable.setId("1234");
    errorable.setError(new ErrorObject("something bad happened", new String[0]));
    handler.handle(errorable);
    handler.handle(errorable);
    verify(taskDispatcher,times(0)).dispatch(any());
  }
  
  @Test
  public void test2 () {
    when(jobRepo.findJobByTaskId("1234")).thenReturn(new MutableJob());
    JobTaskErrorHandler handler = new JobTaskErrorHandler();
    handler.setJobRepository(jobRepo);
    handler.setJobTaskRepository(taskRepo);
    handler.setTaskDispatcher(taskDispatcher);
    MutableJobTask errorable = MutableJobTask.createFrom("retry", 1);
    errorable.setId("1234");
    errorable.setError(new ErrorObject("something bad happened", new String[0]));
    handler.handle(errorable);
    verify(taskDispatcher,times(1)).dispatch(any());
  }
  
}
