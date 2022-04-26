package com.creactiviti.piper.core.pipeline;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PipelineServiceTest {

    @Autowired
    private PipelineService pipelineService;

    @Test
    public void test() {
        ResourceBasedPipelineRepository pipelineRepository = new ResourceBasedPipelineRepository();
        pipelineService.save(pipelineRepository.findOne("demo/cron"));
    }
}
