package com.creactiviti.piper.core.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

class MyJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            System.out.println("MyJob, context = " + new ObjectMapper().writeValueAsString(context.getMergedJobDataMap()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

@SpringBootTest
public class QuartzServiceTest {
    @Autowired
    private QuartzService quartzService;

    @Test
    void testAddJob() throws InterruptedException {
        quartzService.addJob(MyJob.class, "workflowJob", "workflowGroup", 1, 1, Collections.singletonMap("foo", "bar"));

        List<Map<String, Object>> allJob = quartzService.queryAllJob();
        System.out.println("allJob = " + allJob);
        Assertions.assertEquals(1, allJob.size());
        Assertions.assertEquals(allJob.get(0).get("jobStatus"), "NORMAL");
        Thread.sleep(1100);
        allJob = quartzService.queryAllJob();
        System.out.println("allJob = " + allJob);
        Assertions.assertEquals(0, allJob.size(), "job should be removed");
    }
}
