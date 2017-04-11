package com.creactiviti.piper.error;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.job.MutableJob;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.task.JobTaskRepository;

public class JobTaskErrorHandlerTests {

  private JobRepository jobRepo = mock(JobRepository.class);
  private JobTaskRepository taskRepo = mock(JobTaskRepository.class);
  
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
    verify(jobRepo,atLeastOnce()).update(any());
    verify(taskRepo,atLeastOnce()).update(any());
  }
 
  
}