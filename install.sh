#!/bin/bash

mvn install -N -f pom.xml 
if [ $? -ne 0 ]; then
    return 0
fi

mvn install -f auth-service-remote/pom.xml
if [ $? -ne 0 ]; then
    return 0
fi

mvn install -f auth-service-messaging/pom.xml

