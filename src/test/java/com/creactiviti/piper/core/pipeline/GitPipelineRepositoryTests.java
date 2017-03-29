package com.creactiviti.piper.core.pipeline;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.creactiviti.piper.git.GitOperations;

public class GitPipelineRepositoryTests {

  ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
  
  @Test
  public void test1 () {
    GitPipelineRepository r = new GitPipelineRepository();
    r.setGitOperations(new GitOperations() {
      @Override
      public List<IdentifiableResource> getHeadFiles(String aUrl, String... aSearchPath) {
        return Arrays.asList(new IdentifiableResource("demo/hello/123", resolver.getResource("file:pipelines/demo/hello.yaml")));
      }
      
      @Override
      public IdentifiableResource getFile(String aUrl, String aFileId) {
        return new IdentifiableResource("demo/hello/123", resolver.getResource("file:pipelines/demo/hello.yaml"));
      }
    });
    List<Pipeline> findAll = r.findAll();
    Assert.assertEquals("demo/hello",findAll.iterator().next().getId());
  }
}
