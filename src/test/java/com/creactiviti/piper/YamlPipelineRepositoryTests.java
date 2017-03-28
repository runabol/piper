package com.creactiviti.piper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.pipeline.GitPipelineRepository;

public class YamlPipelineRepositoryTests {

  @Test
  public void test1 () {
    GitPipelineRepository r = new GitPipelineRepository();
    List<Pipeline> findAll = r.findAll();
    Assert.assertEquals("demo/hello",findAll.iterator().next().getId());
  }
}
