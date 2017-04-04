package com.creactiviti.piper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.Worker;
import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.task.TaskHandlerResolver;

@Configuration
@ConditionalOnPredicate(OnWorkerPredicate.class)
public class WorkerConfiguration {
  
  @Bean
  Worker worker (TaskHandlerResolver aTaskHandlerResolver, Messenger aMessenger) {
    Worker worker = new Worker();
    worker.setMessenger(aMessenger);
    worker.setTaskHandlerResolver(aTaskHandlerResolver);
    return worker;
  }
  
}
