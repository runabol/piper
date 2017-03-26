#!/bin/bash

curl -s -X POST -H Content-Type:application/json -d '{"pipeline":"'"$1"'"}' http://localhost:8080/job/start | jq .
