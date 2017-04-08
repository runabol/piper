package com.creactiviti.piper.stats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpManagementOperations;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.config.ConditionalOnCoordinator;
import com.creactiviti.piper.stats.Stats.Builder;

@Component
@ConditionalOnCoordinator
@ConditionalOnProperty(name="piper.messenger.provider", havingValue="amqp")
public class AmqpStatsContributor implements StatsContributor {

  @Autowired
  private AmqpManagementOperations amqpManagementOperations;
  
  @Autowired
  private AmqpAdmin admin;
  
  @Override
  public void contribute(Builder aBuilder) {
    List<Queue> queues = amqpManagementOperations.getQueues();
    aBuilder.withDetail("queues", queues.stream().map(this::queueDetails).collect(Collectors.toList()));
  }
  
  private Map<String, Object> queueDetails (Queue aQueue) {
    Properties queueProperties = admin.getQueueProperties(aQueue.getName());
    Map map = new HashMap<>(queueProperties);
    return map;
  }

}
