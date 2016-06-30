package com.creactiviti.piper;

import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.creactiviti.piper.core.HornetQMessenger;
import com.creactiviti.piper.core.SimpleTask;
import com.creactiviti.piper.core.Worker;
import com.google.common.base.Throwables;

@Configuration
public class HornetQMessengerConfiguration {

  @Autowired
  private ConnectionFactory connectionFactory;
  
  @Autowired
  private Worker worker;

  @Bean
  HornetQMessenger hornetQMessenger () {
    return new HornetQMessenger();
  }

  @Bean
  DefaultMessageListenerContainer workerMessageListener () {
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
    container.setConnectionFactory (connectionFactory);
    container.setDestinationName("tasks");
    container.setMessageListener (new WorkerMessageListener(worker));
    return container;
  }

  private static class WorkerMessageListener implements MessageListener {

    private final Worker worker;
    
    public WorkerMessageListener(Worker aWorker) {
      worker = aWorker;
    }
    
    @Override
    public void onMessage(Message aMessage) {
      try {
        worker.handle(new SimpleTask(aMessage.getBody(Map.class)));
      } catch (JMSException e) {
        throw Throwables.propagate(e);
      }
    }
    
  }
  
}
