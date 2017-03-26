package com.creactiviti.piper.core;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.task.JobTask;

@Component
public class DefaultWorker implements Worker {

  private TaskHandlerResolver taskHandlerResolver;
  private Messenger messenger;

  @Override
  public void handle (JobTask aTask) {
    TaskHandler<?> taskHandler = taskHandlerResolver.resolve(aTask);
    try {
      Object output = taskHandler.handle(aTask);
      MutableJobTask completion = new MutableJobTask(aTask);
      if(output!=null) {
        completion.setOutput(output);
      }
      completion.setCompletionDate(new Date());
      messenger.send("completions", completion);
    }
    catch (Exception e) {
      MutableJobTask jobTask = new MutableJobTask(aTask);
      jobTask.setException(e);
      messenger.send("errors", jobTask);
    }
  }

  @Override
  public void cancel (String aTaskId) {}

  @Autowired
  public void setTaskHandlerResolver(TaskHandlerResolver aTaskHandlerResolver) {
    taskHandlerResolver = aTaskHandlerResolver;
  }

  @Lazy
  @Autowired  
  public void setMessenger(Messenger aMessenger) {
    messenger = aMessenger;
  }

}
