package com.creactiviti.piper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
import com.creactiviti.piper.core.job.SimpleJobTask;
import com.creactiviti.piper.core.messenger.HornetQMessenger;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.jms.JmsMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

@Configuration
@EnableConfigurationProperties(PiperProperties.class)
public class HornetQMessengerConfiguration {

  @Autowired
  private ConnectionFactory connectionFactory;
  
  @Autowired
  private Worker worker;
  
  @Autowired
  private Coordinator coordinator;
  
  private final ObjectMapper json = new ObjectMapper ();
  
  @Bean
  HornetQMessenger hornetQMessenger () {
    return new HornetQMessenger();
  }
  
  @Bean
  JmsMessageConverter jmsMessageConverter (PiperProperties piperProperties) {
    return new JmsMessageConverter(piperProperties.getSerialization().getDateFormat());
  }

  @Bean
  JmsTemplate jmsTemplate (PiperProperties piperProperties) {
    JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
    jmsTemplate.setMessageConverter(jmsMessageConverter(piperProperties));
    return jmsTemplate;
  }
  
  @Bean
  DefaultMessageListenerContainer workerMessageListener () {
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
    container.setConnectionFactory (connectionFactory);
    container.setDestinationName("tasks");
    MessageListener listener = (m) -> worker.handle(toTask(m));
    container.setMessageListener(listener);
    return container;
  }
  
  @Bean
  DefaultMessageListenerContainer completionsMessageListener () throws JMSException {
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
    container.setConnectionFactory (connectionFactory);
    container.setDestinationName("completions");
    MessageListener listener = (m) -> coordinator.complete(toTask(m));
    container.setMessageListener(listener);
    return container;
  }
  
  private JobTask toTask (Message aMessage) {
    try {
      Map<String,Object> raw = aMessage.getBody(Map.class);
      Map<String, Object> task = new HashMap<>();
      for(Entry<String,Object> entry : raw.entrySet()) {
        if(entry.getValue().toString().startsWith("list:")) {
          try {
            task.put(entry.getKey(), json.readValue((String)entry.getValue().toString().substring(5),ArrayList.class));
          } catch (IOException e) {
            throw Throwables.propagate(e);
          }
        }
        else {
          task.put(entry.getKey(), entry.getValue());
        }
      }
      return new SimpleJobTask(task);
    } catch (JMSException e) {
      throw Throwables.propagate(e);
    }
  }
    
}
