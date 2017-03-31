/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.messenger.SynchMessenger;
import com.creactiviti.piper.core.task.JobTask;

public class WorkerTests {

  @Test
  public void test1 () {
    Worker worker = new Worker();
    SynchMessenger messenger = new SynchMessenger();
    messenger.receive("completions", (t)-> Assert.assertTrue(((JobTask)t).getOutput().equals("done")) );
    worker.setMessenger(messenger);
    worker.setTaskHandlerResolver((jt) -> (t) -> "done");
    worker.handle(new MutableJobTask(Collections.EMPTY_MAP));
  }
  
  
  @Test
  public void test2 () {
    Worker worker = new Worker();
    SynchMessenger messenger = new SynchMessenger();
    messenger.receive("errors", (t)-> Assert.assertTrue( ((JobTask)t).getException().getMessage().equals("bad input") ) );
    worker.setMessenger(messenger);
    worker.setTaskHandlerResolver((jt) -> (t) -> {
      throw new IllegalArgumentException("bad input");
    });
    worker.handle(new MutableJobTask(Collections.EMPTY_MAP));
  }
  
  
}
