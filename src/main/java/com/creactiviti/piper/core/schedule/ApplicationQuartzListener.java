package com.creactiviti.piper.core.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * spring容器加载完毕后事件，启动任务调用
 *
 * @author Tellsea
 * @date 2019/9/7
 */
@Configuration
public class ApplicationQuartzListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private QuartzService quartzService;
    /**
     * 初始启动quartz
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        quartzService.startScheduler();
    }
}