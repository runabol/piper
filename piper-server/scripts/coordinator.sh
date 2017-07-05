#!/bin/bash

java -jar -Djava.security.egd=file:/dev/./urandom -Dpiper.messenger.provider=amqp -Dpiper.coordinator.enabled=true -Dserver.port=8080 -Dpiper.worker.enabled=false -Dpiper.pipeline-repository.git.enabled=true target/piper-server-0.0.1-SNAPSHOT.jar 
