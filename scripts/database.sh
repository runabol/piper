#!/bin/bash

docker rm -f mysql

docker run --name mysql \
  -e MYSQL_DATABASE=db_example \
  -e MYSQL_ROOT_PASSWORD=admin123 \
  -p 3306:3306 \
  -d mysql:8
