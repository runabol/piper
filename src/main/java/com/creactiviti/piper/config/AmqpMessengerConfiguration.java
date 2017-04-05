package com.creactiviti.piper.config;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creactiviti.piper.core.Coordinator;
import com.creactiviti.piper.core.Worker;
import com.creactiviti.piper.core.messenger.AmqpMessenger;
import com.creactiviti.piper.core.task.JobTask;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableRabbit
@EnableConfigurationProperties(PiperProperties.class)
@ConditionalOnProperty(name="piper.messenger.provider",havingValue="amqp")
public class AmqpMessengerConfiguration implements RabbitListenerConfigurer {
  
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
  
  private static final Pattern ROLE_PATTERN = Pattern.compile("([a-zA-Z0-9\\.]+)(\\((\\d+)\\))*"); 
  
  @Bean
  RabbitAdmin admin (ConnectionFactory aConnectionFactory) {
    return new RabbitAdmin(aConnectionFactory);
  }
  
  @Bean
  AmqpMessenger amqpMessenger (AmqpTemplate aAmqpTemplate) {
    AmqpMessenger amqpMessenger = new AmqpMessenger();
    amqpMessenger.setAmqpTemplate(aAmqpTemplate);
    return amqpMessenger;
  }
  
  @Bean 
  MessageConverter jacksonAmqpMessageConverter(ObjectMapper aObjectMapper) {
    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
    converter.setJsonObjectMapper(aObjectMapper);
    return converter;
  }
  
  @Bean
  @ConditionalOnCoordinator
  Queue completionsQueue () {
    return new Queue("coordinator.completions");
  }
  
  @Bean
  @ConditionalOnCoordinator
  Queue errorsQueue () {
    return new Queue("coordinator.errors");
  }
  
  @ConditionalOnCoordinator
  @RabbitListener(queues="coordinator.completions")
  public void receiveCompletion (JobTask aTask) {
    coordinator.completeTask(aTask);
  }
  
  @ConditionalOnCoordinator
  @RabbitListener(queues="coordinator.errors")
  public void receiveError (JobTask aTask) {
    coordinator.error(aTask);
  }

  @Override
  public void configureRabbitListeners(RabbitListenerEndpointRegistrar aRegistrar) {
    String[] roles = properties.getRoles();
    for(String role : roles) {
      if(role.startsWith("worker")) {
        registerListenerEndpoint(aRegistrar, role, worker, "handle");
      }
    }    
  }
  
  private void registerListenerEndpoint(RabbitListenerEndpointRegistrar aRegistrar, String aRole, Object aDelegate, String aMethodName) {
    logger.info("Registring AMQP Listener: {} -> {}:{}", aRole, aDelegate.getClass().getName(), aMethodName);
    Matcher qMatcher = ROLE_PATTERN.matcher(aRole);
    if(qMatcher.matches()) {
      String queueName = qMatcher.group(1)+".tasks";
      String concurrency = qMatcher.group(3);
      admin(connectionFactory).declareQueue(new Queue(queueName));
      
      MessageListenerAdapter messageListener = new MessageListenerAdapter(aDelegate);
      messageListener.setMessageConverter(jacksonAmqpMessageConverter(objectMapper));
      messageListener.setDefaultListenerMethod(aMethodName);
      
      SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
      endpoint.setId(queueName+"Endpoint");
      endpoint.setQueueNames(queueName);
      endpoint.setMessageListener(messageListener);
      
      aRegistrar.registerEndpoint(endpoint,createContainerFactory(concurrency!=null?Integer.valueOf(concurrency):1));
    }
    else {
      throw new IllegalArgumentException("Invalid role: " + aRole);
    }
  }
  
  private SimpleRabbitListenerContainerFactory createContainerFactory (int aConcurrency) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConcurrentConsumers(aConcurrency);
    factory.setConnectionFactory(connectionFactory);
    return factory;
  }
  
}
