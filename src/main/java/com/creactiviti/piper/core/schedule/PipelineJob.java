package com.creactiviti.piper.core.schedule;

import com.creactiviti.piper.core.Coordinator;
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PipelineJob(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Executing pipeline job. data: {}", context.getMergedJobDataMap());
        Coordinator coordinator = applicationContext.getBean("coordinator", Coordinator.class);
        coordinator.create(context.getMergedJobDataMap());
    }
}
