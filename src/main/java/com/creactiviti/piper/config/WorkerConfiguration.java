/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
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
