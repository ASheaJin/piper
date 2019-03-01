#!/bin/sh

#杀进程
ps -ef|grep "piper-1.0-SNAPSHOT.jar" | grep java | awk '{print $2}' | xargs kill -9

#重启
nohup java -Xdebug -Xrunjdwp:transport=dt_socket,suspend=n,server=y,address=8889 -jar target/piper-1.0-SNAPSHOT.jar --spring.profiles.active=dev> pipeline-api.log 2>&1 &
#打印日志
tail -f pipeline-api.log