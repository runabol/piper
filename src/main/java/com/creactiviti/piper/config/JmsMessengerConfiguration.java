/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.Worker;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.messenger.JmsMessenger;
import com.creactiviti.piper.core.task.JobTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

@Configuration
@EnableConfigurationProperties(PiperProperties.class)
public class JmsMessengerConfiguration {

  @Autowired
  private ConnectionFactory connectionFactory;
  
  
  @Bean
  JmsMessenger jmsMessenger (JmsTemplate aJmsTemplate, ObjectMapper aObjectMapper) {
    JmsMessenger jmsMessenger = new JmsMessenger();
    jmsMessenger.setJmsTemplate(aJmsTemplate);
    jmsMessenger.setObjectMapper(aObjectMapper);
    return jmsMessenger;
  }
  
  @Bean
  JmsTemplate jmsTemplate (PiperProperties piperProperties) {
    JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
    return jmsTemplate;
  }
  
  @Bean
  @ConditionalOnPredicate(OnWorkerPredicate.class)
  DefaultMessageListenerContainer workerMessageListener (ObjectMapper aObjectMapper, Worker aWorker) {
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
    container.setConnectionFactory (connectionFactory);
    container.setDestinationName("tasks");
    MessageListener listener = (m) -> aWorker.handle(toTask(m, aObjectMapper));
    container.setMessageListener(listener);
    return container;
  }
  
  @Bean
  @ConditionalOnPredicate(OnCoordinatorPredicate.class)
  DefaultMessageListenerContainer completionsMessageListener (ObjectMapper aObjectMapper, Coordinator aCoordinator) throws JMSException {
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
    container.setConnectionFactory (connectionFactory);
    container.setDestinationName("completions");
    MessageListener listener = (m) -> aCoordinator.completeTask(toTask(m, aObjectMapper));
    container.setMessageListener(listener);
    return container;
  }
  
  @Bean
  @ConditionalOnPredicate(OnCoordinatorPredicate.class)
  DefaultMessageListenerContainer errorsMessageListener (ObjectMapper aObjectMapper, Coordinator aCoordinator) throws JMSException {
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
    container.setConnectionFactory (connectionFactory);
    container.setDestinationName("errors");
    MessageListener listener = (m) -> aCoordinator.error(toTask(m, aObjectMapper));
    container.setMessageListener(listener);
    return container;
  }
  
  private JobTask toTask (Message aMessage, ObjectMapper aObjectMapper) {
    try {
      String raw = aMessage.getBody(String.class);
      Map<String, Object> task = aObjectMapper.readValue(raw,Map.class);
      return new MutableJobTask(task);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
    
}
