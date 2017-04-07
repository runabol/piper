#!/bin/bash

mvn clean package spring-boot:run -Dpiper.messenger.provider=amqp -Dpiper.coordinator.enabled=false -DskipTests
