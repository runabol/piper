package com.creactiviti.piper.config;


import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.messenger.AmqpMessenger;
import com.creactiviti.piper.core.task.JobTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

@Configuration
@EnableConfigurationProperties(PiperProperties.class)
@ConditionalOnProperty(name="piper.messenger.provider",havingValue="amqp")
public class AmqpMessengerConfiguration {
  
  @Bean
  AmqpMessenger amqpMessenger (AmqpTemplate aAmqpTemplate, ObjectMapper aObjectMapper) {
    AmqpMessenger amqpMessenger = new AmqpMessenger();
    amqpMessenger.setAmqpTemplate(aAmqpTemplate);
    amqpMessenger.setObjectMapper(aObjectMapper);
    return amqpMessenger;
  }
    
  @PostConstruct
  @ConditionalOnWorker
  void createWorkerQueues () {
    System.out.println("***********");
  }
  
  @Bean
  @ConditionalOnCoordinator
  Queue completionsQueue () {
    return new Queue("completions");
  }
  
  @Bean
  @ConditionalOnCoordinator
  Queue errorsQueue () {
    return new Queue("errors");
  }
  
  @Bean
  @ConditionalOnCoordinator
  Queue jobsQueue () {
    return new Queue("jobs");
  }
  
  @Bean
  @ConditionalOnCoordinator
  SimpleMessageListenerContainer completionsMessageListener (ConnectionFactory aConnectionFactory, ObjectMapper aObjectMapper, Coordinator aCoordinator) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setQueueNames("completions");
    container.setConnectionFactory(aConnectionFactory);
    MessageListener listener = (m) -> aCoordinator.completeTask(toTask(m, aObjectMapper));
    container.setMessageListener(listener);
    return container;
  }
  
  private JobTask toTask (Message aMessage, ObjectMapper aObjectMapper) {
    try {
      String raw = new String(aMessage.getBody());
      Map<String, Object> task = aObjectMapper.readValue(raw,Map.class);
      return new MutableJobTask(task);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
  
}
