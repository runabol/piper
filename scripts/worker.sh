#!/bin/bash

mvn clean package spring-boot:run -Dpiper.messenger.provider=amqp -Dpiper.coordinator.enabled=false -Dserver.port=9999 -Dpiper.worker.enabled=true -Dpiper.worker.subscriptions.tasks=100
