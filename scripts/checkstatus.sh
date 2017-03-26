#!/bin/bash

curl -s http://localhost:8080/job/$1 | jq .
