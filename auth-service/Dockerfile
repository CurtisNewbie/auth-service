FROM openjdk:8-jdk-alpine
LABEL author="yongjie.zhuang"
LABEL descrption="Authentication Service"
COPY . /usr/src/auth-service
WORKDIR /usr/src/auth-service
EXPOSE 8080/tcp
EXPOSE 9090/tcp
EXPOSE 7011/tcp
CMD ["java", "-jar", "auth-service-1.0.0.jar", "--spring.profiles.active=test"]
