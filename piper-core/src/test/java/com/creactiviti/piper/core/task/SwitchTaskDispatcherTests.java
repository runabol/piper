
package com.creactiviti.piper.core.task;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.context.MapContext;
import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.creactiviti.piper.core.messenger.Messenger;
import com.google.common.collect.ImmutableMap;

public class SwitchTaskDispatcherTests {
  
  private TaskExecutionRepository taskRepo = mock(TaskExecutionRepository.class);
  private TaskDispatcher taskDispatcher = mock(TaskDispatcher.class);
  private Messenger messenger = mock(Messenger.class);
  private ContextRepository contextRepository = mock(ContextRepository.class);
  
  @Test
  public void test1 ()  {
    when(contextRepository.peek(any())).thenReturn(new MapContext());
    SwitchTaskDispatcher dispatcher = new SwitchTaskDispatcher(taskDispatcher, taskRepo,messenger,contextRepository);
    SimpleTaskExecution task = SimpleTaskExecution.create();
    task.set("cases", Arrays.asList(ImmutableMap.of("key", "k1","tasks",Arrays.asList(ImmutableMap.of("type","print")))));
    task.set("expression", "k1");
    dispatcher.dispatch(task);
    ArgumentCaptor<TaskExecution> argument = ArgumentCaptor.forClass(TaskExecution.class);
    verify(taskDispatcher,times(1)).dispatch(argument.capture());
    Assert.assertEquals("print", argument.getValue().getType());
  }
  
  @Test
  public void test2 ()  {
    when(contextRepository.peek(any())).thenReturn(new MapContext());
    SwitchTaskDispatcher dispatcher = new SwitchTaskDispatcher(taskDispatcher, taskRepo,messenger,contextRepository);
    SimpleTaskExecution task = SimpleTaskExecution.create();
    task.set("cases", Arrays.asList(ImmutableMap.of("key", "k1","tasks",Arrays.asList(ImmutableMap.of("type","print")))));
    task.set("expression", "k2");
    dispatcher.dispatch(task);
    verify(taskDispatcher,times(0)).dispatch(any());
  }
  
  @Test
  public void test3 ()  {
    when(contextRepository.peek(any())).thenReturn(new MapContext());
    SwitchTaskDispatcher dispatcher = new SwitchTaskDispatcher(taskDispatcher, taskRepo,messenger,contextRepository);
    SimpleTaskExecution task = SimpleTaskExecution.create();
    task.set("cases", Arrays.asList(
      ImmutableMap.of("key", "k1","tasks",Arrays.asList(ImmutableMap.of("type","print"))),
      ImmutableMap.of("key", "k2","tasks",Arrays.asList(ImmutableMap.of("type","sleep"))))
    );
    task.set("expression", "k2");
    dispatcher.dispatch(task);
    ArgumentCaptor<TaskExecution> argument = ArgumentCaptor.forClass(TaskExecution.class);
    verify(taskDispatcher,times(1)).dispatch(argument.capture());
    Assert.assertEquals("sleep", argument.getValue().getType());
  }
  
  @Test
  public void test4 ()  {
    when(contextRepository.peek(any())).thenReturn(new MapContext());
    SwitchTaskDispatcher dispatcher = new SwitchTaskDispatcher(taskDispatcher, taskRepo,messenger,contextRepository);
    SimpleTaskExecution task = SimpleTaskExecution.create();
    task.set("cases", Arrays.asList(
      ImmutableMap.of("key", "k1","tasks",Arrays.asList(ImmutableMap.of("type","print"))),
      ImmutableMap.of("key", "k2","tasks",Arrays.asList(ImmutableMap.of("type","sleep")))
    ));
    task.set("default", Arrays.asList(ImmutableMap.of("type","var")));
    task.set("expression", "k99");
    dispatcher.dispatch(task);
    ArgumentCaptor<TaskExecution> argument = ArgumentCaptor.forClass(TaskExecution.class);
    verify(taskDispatcher,times(1)).dispatch(argument.capture());
    Assert.assertEquals("var", argument.getValue().getType());
  }
  
}