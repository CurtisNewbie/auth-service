#!/bin/bash

jarname="auth-service-1.0.3-SNAPSHOT.jar"

mvn clean package -Dmaven.test.skip=true

scp "target/${jarname}" "zhuangyongj@curtisnewbie.com:~/services/auth-service/auth-service.jar"
