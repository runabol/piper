/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import javax.jms.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.Worker;
import com.creactiviti.piper.core.messenger.JmsMessenger;
import com.creactiviti.piper.core.task.JobTask;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableJms
@EnableConfigurationProperties(PiperProperties.class)
@ConditionalOnProperty(name="piper.messenger.provider",havingValue="jms")
public class JmsMessengerConfiguration implements JmsListenerConfigurer {

  @Autowired
  @ConditionalOnWorker
  private Worker worker;
  
  @Autowired
  @ConditionalOnCoordinator
  private Coordinator coordinator;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private PiperProperties properties;
  
  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Bean
  JmsMessenger jmsMessenger (JmsTemplate aJmsTemplate, ObjectMapper aObjectMapper) {
    JmsMessenger jmsMessenger = new JmsMessenger();
    jmsMessenger.setJmsTemplate(aJmsTemplate);
    return jmsMessenger;
  }
  
  @Bean 
  public MessageConverter jacksonJmsMessageConverter(ObjectMapper aObjectMapper) {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setObjectMapper(aObjectMapper);
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    return converter;
  }

  @Bean
  public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    configurer.configure(factory, connectionFactory);
    return factory;
  }

  @ConditionalOnWorker
  @JmsListener(destination="tasks")
  public void receiveTask (JobTask aTask) {
    worker.handle(aTask);
  }

  @ConditionalOnCoordinator
  @JmsListener(destination="completions")
  public void receiveCompletion (JobTask aTask) {
    coordinator.completeTask(aTask);
  }

  @Override
  public void configureJmsListeners(JmsListenerEndpointRegistrar aRegistrar) {
    String[] roles = properties.getRoles();
    for(String role : roles) {
      if(role.equals("worker")) {
        aRegistrar.registerEndpoint(createListenerEndpoint("tasks", worker, "handle"));
      }
      else if (role.startsWith("worker.")) {
        String destination = "tasks." + role.substring(role.indexOf('.')+1);
        aRegistrar.registerEndpoint(createListenerEndpoint(destination, worker, "handle"));
      }
    }
  }

  private JmsListenerEndpoint createListenerEndpoint(String aDestination, Object aDelegate, String aMethodName) {
    logger.info("Registring JMS Listener: {} -> {}:{}", aDestination, aDelegate.getClass().getName(), aMethodName);
    SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
    endpoint.setId(aDestination+"Endpoint");
    endpoint.setDestination(aDestination);
    MessageListenerAdapter messageListener = new MessageListenerAdapter(aDelegate);
    messageListener.setMessageConverter(jacksonJmsMessageConverter(objectMapper));
    messageListener.setDefaultListenerMethod(aMethodName);
    endpoint.setMessageListener(messageListener);
    return endpoint;
  }
    
}
