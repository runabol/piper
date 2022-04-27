package com.creactiviti.piper.core.schedule;

import com.creactiviti.piper.core.Coordinator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class PipelineJob extends QuartzJobBean {

    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PipelineJob(ApplicationContext applicationContext, ObjectMapper objectMapper) {
        this.applicationContext = applicationContext;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.info("Executing pipeline job. data: {}", objectMapper.writeValueAsString(context.getMergedJobDataMap()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Coordinator coordinator = applicationContext.getBean("coordinator", Coordinator.class);
        coordinator.create(context.getMergedJobDataMap());
    }
}
