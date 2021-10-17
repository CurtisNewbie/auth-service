FROM openjdk:8-jdk-alpine
LABEL author="yongjie.zhuang"
LABEL descrption="Authentication Service Web"
COPY . /usr/src/auth-service-web
WORKDIR /usr/src/auth-service-web
EXPOSE 8443/tcp
EXPOSE 9091/tcp
CMD ["java", "-jar", "auth-service-web-1.0.0.jar", "--spring.profiles.active=test"]
