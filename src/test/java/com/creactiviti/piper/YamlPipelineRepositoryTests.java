package com.creactiviti.piper;

import java.util.List;

import org.junit.Test;

import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.pipeline.YamlPipelineRepository;

public class YamlPipelineRepositoryTests {

  @Test
  public void test1 () {
    YamlPipelineRepository r = new YamlPipelineRepository();
    r.setPath("git@github.com:creactiviti/piper-pipelines.git/demo/**");
    List<Pipeline> findAll = r.findAll();
    findAll.forEach(p->System.out.println(p.getId()));
  }
  
}
