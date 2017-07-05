#!/bin/bash

java -jar \
     -Djava.security.egd=file:/dev/./urandom \
     -Dpiper.messenger.provider=amqp \
     -Dpiper.coordinator.enabled=false \
     -Dserver.port=8181 \
     -Dpiper.worker.enabled=true \
     -Dpiper.worker.subscriptions.tasks=200 \
     piper-server/target/piper-server-0.0.1-SNAPSHOT.jar 
