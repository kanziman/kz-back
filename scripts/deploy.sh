#!/bin/bash

REPOSITORY=/home/ec2-user/app/step4
PROJECT_NAME=kz-app

echo "> START"
java -jar -Dserver.port=8082 \
-Dspring.config.location=/home/ec2-user/app/application-prod.yml \
-Dspring.profiles.active=prod \
 /home/ec2-user/app/step4/zip/build/libs/kz-1.0.jar

echo "> CHECK"
 curl -s -o /dev/null -w "%{http_code}"  http://127.0.0.1:8082/health_check