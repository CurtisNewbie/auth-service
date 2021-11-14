#!/bin/bash
#remoteaddr="192.168.10.128"
remoteaddr="192.168.31.103"
jarname="auth-service-web-1.0.2.jar"

mvn clean package -Dmaven.test.skip=true

mvnpkg=$?

if [ ! $mvnpkg -eq 0 ] 
then
    exit -1
fi

scp "target/${jarname}" "zhuangyongj@${remoteaddr}:~/services/auth-service-web/auth-service-web.jar"
