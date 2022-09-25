FROM openjdk:17-alpine  

LABEL author="yongjie.zhuang"
LABEL descrption="Authentication Service"

COPY . /usr/src/auth-service
WORKDIR /usr/src/auth-service

ENV TZ=Asia/Shanghai

# Rewrite this in docker-compose, and specify the port explicitly
CMD ["java", "-jar", "auth-service.jar", "--spring.profiles.active=test"]
