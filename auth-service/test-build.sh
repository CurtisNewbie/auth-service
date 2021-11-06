#!/bin/bash

jarname="auth-service-1.0.1.jar"

mvn clean package -Dmaven.test.skip=true

# ssh -l zhuangyongj 192.168.10.128 "/home/zhuangyongj/exec/authserver/kill-as.sh"

scp "target/${jarname}" "zhuangyongj@curtisnewbie.com:~/services/auth-service/auth-service.jar"
