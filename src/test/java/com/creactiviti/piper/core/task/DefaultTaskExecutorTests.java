/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.task;

import org.junit.Assert;
import org.junit.Test;

import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.messenger.Queues;

public class DefaultTaskExecutorTests {

  @Test
  public void test1 () {
    DefaultTaskExecutor executor = new DefaultTaskExecutor();
    executor.setMessenger((k,m)->Assert.assertEquals(Queues.TASKS, k));
    executor.execute(new MutableJobTask());
  }
  
  @Test
  public void test2 () {
    MutableJobTask task = new MutableJobTask();
    task.setNode("encoder");
    DefaultTaskExecutor executor = new DefaultTaskExecutor();
    executor.setMessenger((k,m)->Assert.assertEquals("encoder", k));
    executor.execute(task);
  }
  
  @Test
  public void test3 () {
    MutableJobTask task = new MutableJobTask();
    task.setNode("encoder.xlarge");
    DefaultTaskExecutor executor = new DefaultTaskExecutor();
    executor.setMessenger((k,m)->Assert.assertEquals("encoder.xlarge", k));
    executor.execute(task);
  }
  
}
