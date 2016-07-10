package com.creactiviti.piper.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.job.SimpleJobTask;
import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.task.JobTask;

@Component
public class DefaultWorker implements Worker {

  private Map<String, TaskHandler<?>> taskHandlers = new HashMap<String, TaskHandler<?>>();
  private Messenger messenger;
  
  @Override
  public void handle (JobTask aTask) {
    TaskHandler<?> taskHandler = taskHandlers.get(aTask.getHandler());
    Assert.notNull(taskHandler,"Unknown task handler: " + aTask.getHandler());
    Object output = taskHandler.handle(aTask);
    SimpleJobTask completion = new SimpleJobTask(aTask);
    if(output!=null) {
      completion.setOutput(output);
    }
    completion.setCompletionDate(new Date());
    messenger.send("completions", completion);
  }
  
  @Override
  public void cancel (String aTaskId) {}

  @Autowired(required=false)
  public void setTaskHandlers(Map<String, TaskHandler<?>> aTaskHandlers) {
    taskHandlers = aTaskHandlers;
  }
  
  @Lazy
  @Autowired  
  public void setMessenger(Messenger aMessenger) {
    messenger = aMessenger;
  }

}
