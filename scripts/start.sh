#!/bin/bash

curl -s -X POST -H Content-Type:application/json -d '{"pipelineId":"'"$1"'"}' http://localhost:8080/jobs | jq .
