#!/bin/bash
jarname="auth-service-web-1.0.3-SNAPSHOT.jar"

mvn clean package -Dmaven.test.skip=true

mvnpkg=$?

if [ ! $mvnpkg -eq 0 ] 
then
    exit -1
fi

scp "target/${jarname}" "zhuangyongj@curtisnewbie.com:~/services/auth-service-web/auth-service-web.jar"
