package com.creactiviti.piper.core.taskhandler;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.google.common.collect.ImmutableMap;

public class SwitchTests {

  @Test
  public void test1 () throws Exception {
    Switch s = new Switch();
    SimpleTaskExecution t = SimpleTaskExecution.create();
    t.set("expression", "a");
    t.set("default", "b");
    t.set("cases", new ArrayList<>());
    Object result = s.handle(t);
    Assert.assertEquals("b", result);
  }
  
  @Test
  public void test2 () throws Exception {
    Switch s = new Switch();
    SimpleTaskExecution t = SimpleTaskExecution.create();
    t.set("expression", "a");
    t.set("default", "b");
    t.set("cases", Collections.singletonList(ImmutableMap.of("key", "a", "value", "c")));
    Object result = s.handle(t);
    Assert.assertEquals("c", result);
  }
  
  
}