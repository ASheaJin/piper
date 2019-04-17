#!/bin/sh

#java -Xdebug -Xrunjdwp:transport=dt_socket,suspend=n,server=y,address=8889 -jar target/piper-1.5.2-SNAPSHOT.jar
#nohup java -agentpath:/opt/jprofiler9/bin/linux-x64/libjprofilerti.so=port=10012,nowait -jar target/piper-1.5.2-SNAPSHOT.jar > pipeline-api.log 2>&1 &

#杀进程
ps -ef|grep "piper-1.5.2-SNAPSHOT.jar" | grep java | awk '{print $2}' | xargs kill -9
#打包
/usr/maven/bin/mvn -U package
#重启
nohup java -Xdebug -Xrunjdwp:transport=dt_socket,suspend=n,server=y,address=8889 -jar target/piper-1.5.15-SNAPSHOT.jar -Dspring.profiles.active=dev> pipeline-api.log 2>&1 &
#打印日志
tail -f pipeline-api.log
