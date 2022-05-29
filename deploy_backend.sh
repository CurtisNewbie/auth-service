#!/bin/bash

remote="alphaboi@curtisnewbie.com"
remote_path="~/services/auth-service/build/auth-service.jar"

jarname="auth-service-build.jar"

# dependencies
mvn install -Dmaven.test.skip=true

# packaging
mvn package -f auth-service/pom.xml -Dmaven.test.skip=true

# copy to remove server
scp "auth-service/target/${jarname}" "${remote}:${remote_path}"
