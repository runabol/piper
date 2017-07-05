/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.Worker;
import com.creactiviti.piper.core.annotations.ConditionalOnWorker;
import com.creactiviti.piper.core.event.DistributedEventPublisher;
import com.creactiviti.piper.core.messenger.Messenger;
import com.creactiviti.piper.core.task.TaskHandlerResolver;

@Configuration
@ConditionalOnWorker
public class WorkerConfiguration {
  
  @Autowired private Messenger messenger;
  
  @Bean
  Worker worker (TaskHandlerResolver aTaskHandlerResolver, Messenger aMessenger) {
    Worker worker = new Worker();
    worker.setMessenger(aMessenger);
    worker.setTaskHandlerResolver(aTaskHandlerResolver);
    worker.setEventPublisher(workerEventPublisher());
    return worker;
  }

  @Bean
  DistributedEventPublisher workerEventPublisher () {
    return new DistributedEventPublisher (messenger);
  }
}
