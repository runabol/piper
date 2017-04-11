package com.creactiviti.piper.error;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.creactiviti.piper.core.job.MutableJob;
import com.creactiviti.piper.core.job.MutableJobTask;

public class ErrorHandlerChainTests {

  @Test
  public void test1() {
    ErrorHandler errorHandler = (j) -> {
      Assert.assertEquals(MutableJob.class, j.getClass());
    };
    ErrorHandlerChain chain = new ErrorHandlerChain(Arrays.asList(errorHandler));
    chain.handle(new MutableJob());
  }

  @Test
  public void test2() {
    ErrorHandler errorHandler = (jt) -> {
      Assert.assertEquals(MutableJobTask.class, jt.getClass());
    };
    ErrorHandlerChain chain = new ErrorHandlerChain(Arrays.asList(errorHandler));
    chain.handle(new MutableJobTask());
  }
}
