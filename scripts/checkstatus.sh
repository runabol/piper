#!/bin/bash

curl -s http://localhost:8080/jobs/$1 | jq .
