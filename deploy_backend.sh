#!/bin/bash

remote="alphaboi@curtisnewbie.com"
remote_path="~/services/auth-service/build/auth-service.jar"

jarname="auth-service-build.jar"

# dependencies
mvn install -Dmaven.test.skip=true
if [ ! $? -eq 0 ]; then
    exit -1
fi


# packaging
mvn package -f auth-service/pom.xml -Dmaven.test.skip=true
if [ ! $? -eq 0 ]; then
    exit -1
fi


# copy to remove server
scp "auth-service/target/${jarname}" "${remote}:${remote_path}"
if [ ! $? -eq 0 ]; then
    exit -1
fi

ssh  "alphaboi@curtisnewbie.com" "cd services; docker-compose up -d --build auth-service"
