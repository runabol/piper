package com.creactiviti.piper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.pipeline.YamlPipelineRepository;

public class YamlPipelineRepositoryTests {

  @Test
  public void test1 () {
    YamlPipelineRepository r = new YamlPipelineRepository();
    List<Pipeline> findAll = r.findAll();
    Assert.assertEquals("demo/hello",findAll.iterator().next().getId());
  }
}
