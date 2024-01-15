#!/bin/bash

CURRENT_PORT=$(cat /home/ec2-user/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo "> Kill WAS running at ${TARGET_PORT}."
  sudo kill ${TARGET_PID}
fi

echo "> start! ${TARGET_PORT}."
chmod +x /home/ec2-user/app/step4/zip/build/libs/kz-1.0.jar

nohup java -jar -Dserver.port=${TARGET_PORT} \
-Dspring.config.location=/home/ec2-user/app/application-prod.yml \
-Dspring.profiles.active=prod \
 /home/ec2-user/app/step4/zip/build/libs/kz-1.0.jar > /home/ec2-user/nohup.out 2>&1 &
echo "> Now new WAS runs at ${TARGET_PORT}."
curl -s -o /dev/null -w "%{http_code}"  http://127.0.0.1:8082/health_check
echo "> END"
exit 0


