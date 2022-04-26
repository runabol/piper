package com.creactiviti.piper.taskhandler.trigger;

import com.creactiviti.piper.core.task.EbotTaskHandler;
import com.creactiviti.piper.core.task.TaskExecution;
import org.springframework.stereotype.Component;


/**
 * @author Bohan
 * @since 2022-04-25
 */
@Component("trigger/cron")
public class Cron implements EbotTaskHandler<Object> {

    @Override
    public Object handle(TaskExecution aTask) throws Exception {
        String cron = aTask.getRequiredString("cron");
        System.out.println("cron: " + cron);
        return null;
    }
}
