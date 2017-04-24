/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core;

import org.junit.Assert;
import org.junit.Test;

import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.messenger.Queues;
import com.creactiviti.piper.core.messenger.SynchMessenger;
import com.creactiviti.piper.core.task.JobTask;

public class WorkerTests {

  @Test
  public void test1 () {
    Worker worker = new Worker();
    SynchMessenger messenger = new SynchMessenger();
    messenger.receive(Queues.COMPLETIONS, (t)-> Assert.assertTrue(((JobTask)t).getOutput().equals("done")) );
    messenger.receive(Queues.EVENTS, (t)-> {} );
    worker.setMessenger(messenger);
    worker.setTaskHandlerResolver((jt) -> (t) -> "done");
    MutableJobTask task = MutableJobTask.create();
    task.setId("1234");
    worker.handle(task);
  }
  
  
  @Test
  public void test2 () {
    Worker worker = new Worker();
    SynchMessenger messenger = new SynchMessenger();
    messenger.receive(Queues.ERRORS, (t)-> Assert.assertTrue( ((JobTask)t).getError().getMessage().equals("bad input") ) );
    messenger.receive(Queues.EVENTS, (t)-> {} );
    worker.setMessenger(messenger);
    worker.setTaskHandlerResolver((jt) -> (t) -> {
      throw new IllegalArgumentException("bad input");
    });
    MutableJobTask task = MutableJobTask.create();
    task.setId("1234");
    worker.handle(task);
  }
  
  
}
