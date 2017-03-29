package com.creactiviti.piper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.creactiviti.piper.core.pipeline.GitPipelineRepository;
import com.creactiviti.piper.core.pipeline.Pipeline;

public class YamlPipelineRepositoryTests {

  @Test
  public void test1 () {
    GitPipelineRepository r = new GitPipelineRepository();
    r.setUrl("git@github.com:creactiviti/piper-pipelines.git");
    r.setSearchPaths(new String[]{"demo"});
    List<Pipeline> findAll = r.findAll();
    Assert.assertTrue(findAll.iterator().next().getId().startsWith("demo/hello"));
  }
}
