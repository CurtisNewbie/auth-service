#!/bin/bash
#remoteaddr="192.168.10.128"
remoteaddr="192.168.31.103"

mvn clean package 

mvnpkg=$?

if [ ! $mvnpkg -eq 0 ] 
then
    exit -1
fi

scp "target/auth-service-web-1.0.0.jar" "zhuangyongj@${remoteaddr}:~/services/auth-service-web/auth-service-web-1.0.0.jar"
