FROM registry.cn-qingdao.aliyuncs.com/shequtong_registry/cnetos_jdk1.8
LABEL description="use centos_jdk1.8 build app images"
RUN mkdir /app_data
ADD piper-1.6.13-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD java $JAVA_OPTS -jar app.jar