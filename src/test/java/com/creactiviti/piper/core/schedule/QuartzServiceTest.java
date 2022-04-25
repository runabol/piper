package com.creactiviti.piper.core.schedule;

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
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("MyJob, jobExecutionContext = " + jobExecutionContext);
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
