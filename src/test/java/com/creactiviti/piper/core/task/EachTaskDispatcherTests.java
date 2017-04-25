/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.task;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.messenger.Messenger;

public class EachTaskDispatcherTests {
  
  private JobTaskRepository taskRepo = mock(JobTaskRepository.class);
  private TaskDispatcher taskDispatcher = mock(TaskDispatcher.class);
  private Messenger messenger = mock(Messenger.class);
  
  @Test(expected=IllegalArgumentException.class)
  public void test1 ()  {
    EachTaskDispatcher dispatcher = new EachTaskDispatcher(null,null,null);
    dispatcher.dispatch(MutableJobTask.create());
  }
  
  @Test
  public void test2 ()  {
    EachTaskDispatcher dispatcher = new EachTaskDispatcher(taskDispatcher, taskRepo,messenger);
    MutableJobTask task = MutableJobTask.create();
    task.set("list", Arrays.asList(1,2,3));
    task.set("iteratee", Collections.singletonMap("type", "print"));
    dispatcher.dispatch(task);
    verify(taskDispatcher,times(3)).dispatch(any());
    verify(messenger,times(0)).send(any(),any());
  }
  
  @Test
  public void test3 ()  {
    EachTaskDispatcher dispatcher = new EachTaskDispatcher(taskDispatcher, taskRepo,messenger);
    MutableJobTask task = MutableJobTask.create();
    task.set("list", Arrays.asList());
    task.set("iteratee", Collections.singletonMap("type", "print"));
    dispatcher.dispatch(task);
    verify(taskDispatcher,times(0)).dispatch(any());
    verify(messenger,times(1)).send(any(),any());
  }

}
