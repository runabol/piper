package com.creactiviti.piper.config;

import org.junit.Assert;
import org.junit.Test;

public class RoleParserTests {

  @Test
  public void test1 () {
    String queueName = RoleParser.queueName("worker");
    int concurrency = RoleParser.concurrency("worker");
    Assert.assertEquals("worker.tasks", queueName);
    Assert.assertEquals(1, concurrency);
  }
  
  @Test
  public void test2 () {
    String queueName = RoleParser.queueName("worker.encoder(3)");
    int concurrency = RoleParser.concurrency("worker.encoder(3)");
    Assert.assertEquals("worker.encoder.tasks", queueName);
    Assert.assertEquals(3, concurrency);
  }
  
}
