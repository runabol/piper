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
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.util.Assert;

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
  
  @Autowired
  private ConnectionFactory connectionFactory;
  
  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Bean
  JmsMessenger jmsMessenger (JmsTemplate aJmsTemplate) {
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

  @ConditionalOnCoordinator
  @JmsListener(destination="coordinator.completions")
  public void receiveCompletion (JobTask aTask) {
    coordinator.completeTask(aTask);
  }
  
  @ConditionalOnCoordinator
  @JmsListener(destination="coordinator.errors")
  public void receiveError (JobTask aTask) {
    coordinator.error(aTask);
  }

  @Override
  public void configureJmsListeners (JmsListenerEndpointRegistrar aRegistrar) {
    String[] roles = properties.getRoles();
    Assert.notNull(roles, "piper.roles must not be null");
    for(String role : roles) {
      if(role.startsWith("worker")) {
        registerListenerEndpoint(aRegistrar, role, worker, "handle");
      }
    }  
  }

  private void registerListenerEndpoint(JmsListenerEndpointRegistrar aRegistrar, String aRole, Object aDelegate, String aMethodName) {
    logger.info("Registring JMS Listener: {} -> {}:{}", aRole, aDelegate.getClass().getName(), aMethodName);
    
    String queueName = RoleParser.queueName(aRole);
    int concurrency = RoleParser.concurrency(aRole);

    MessageListenerAdapter messageListener = new MessageListenerAdapter(aDelegate);
    messageListener.setMessageConverter(jacksonJmsMessageConverter(objectMapper));
    messageListener.setDefaultListenerMethod(aMethodName);

    SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
    endpoint.setId(queueName+"Endpoint");
    endpoint.setDestination(queueName);
    endpoint.setMessageListener(messageListener);

    aRegistrar.registerEndpoint(endpoint,createContainerFactory(concurrency));
  }

  private DefaultJmsListenerContainerFactory createContainerFactory (int aConcurrency) {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setConcurrency(String.valueOf(aConcurrency));
    factory.setConnectionFactory(connectionFactory);
    return factory;
  }
  
}
