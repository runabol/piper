package com.creactiviti.piper.core;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.messenger.Messenger;

@Component
public class DefaultWorker implements Worker {

  @Autowired
  private Map<String, TaskHandler<?>> taskHandlers;
  
  @Lazy
  @Autowired
  private Messenger messenger;
  
  @Override
  public void handle (JobTask aTask) {
    TaskHandler<?> taskHandler = taskHandlers.get(aTask.getHandler());
    Assert.notNull(taskHandler,"Unknown task handler: " + aTask.getHandler());
    Object output = taskHandler.handle(aTask);
    MutableJobTask completion = new MutableJobTask(aTask);
    if(output!=null) {
      completion.setOutput(output);
    }
    messenger.send("completions", completion);
  }

  @Override
  public void cancel (String aTaskId) {
  }

}
