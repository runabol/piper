#!/bin/bash

curl -s -X PUT http://localhost:8080/jobs/$1/stop | jq .
