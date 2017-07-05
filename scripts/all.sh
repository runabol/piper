#!/bin/bash

java -jar -Djava.security.egd=file:/dev/./urandom -Dpiper.messenger.provider=jms -Dpiper.coordinator.enabled=true -Dpiper.worker.enabled=true -Dpiper.worker.subscriptions.tasks=5 -Dpiper.pipeline-repository.git.enabled=false ../piper-server/target/piper-server-0.0.1-SNAPSHOT.jar 
