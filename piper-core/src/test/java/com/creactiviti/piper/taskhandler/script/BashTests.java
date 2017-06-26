package com.creactiviti.piper.taskhandler.script;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.creactiviti.piper.plugin.script.Bash;

public class BashTests {

  @Test
  public void test1 () throws Exception {
    Bash bash = new Bash();
    ClassPathResource cpr = new ClassPathResource("schema.sql");
    String output = bash.handle(SimpleTaskExecution.createFrom ("script", "ls -l " + cpr.getFile().getAbsolutePath()));
    Assert.assertTrue(output.contains("target/test-classes/schema.sql"));
  }
  
}
