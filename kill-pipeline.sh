#!/bin/sh
ps -ef|grep "pipeline-api-1.0-SNAPSHOT.jar" | grep java | awk '{print $2}' | xargs kill -9