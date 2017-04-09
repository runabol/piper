package com.creactiviti.piper.stats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.config.ConditionalOnCoordinator;
import com.creactiviti.piper.stats.Stats.Builder;
import com.google.common.collect.ImmutableMap;
import com.rabbitmq.http.client.Client;
import com.rabbitmq.http.client.domain.QueueInfo;


/**
 * a {@link StatsContributor} implementation which exposes any declared AMQP
 * queues.
 * 
 * @author Arik Cohen
 * @since Apt 8, 2017
 */
@Component
@ConditionalOnCoordinator
@ConditionalOnProperty(name="piper.messenger.provider", havingValue="amqp")
public class AmqpStatsContributor implements StatsContributor {

  private Client client;
  
  @Override
  public void contribute(Builder aBuilder) {
    List<QueueInfo> queues = client.getQueues();
    aBuilder.withDetail("queues", queues.stream().map(this::queueDetails).collect(Collectors.toList()));
  }
  
  private Map<String, Object> queueDetails (QueueInfo aQueue) {
    Map map = new HashMap<>();
    map.put("name", aQueue.getName());
    map.put("consumers", aQueue.getConsumerCount());
    map.put("messages", ImmutableMap.of(
                                         "ready",aQueue.getMessagesReady(),
                                         "unacknowledged",aQueue.getMessagesUnacknowledged(),
                                         "total",aQueue.getTotalMessages()
                                       ));
    return map;
  }

  @Autowired
  public void setClient(Client aClient) {
    client = aClient;
  }

}
