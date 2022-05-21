#!/bin/bash

jarname="auth-service-build.jar"

mvn clean package -Dmaven.test.skip=true

scp "target/${jarname}" "zhuangyongj@curtisnewbie.com:~/services/auth-service/build/auth-service.jar"
