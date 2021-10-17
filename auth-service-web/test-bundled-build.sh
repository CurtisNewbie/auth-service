#!/bin/bash

staticpath="src/main/resources/static/"

if [ -d $staticpath ]; then
    rm -r $staticpath 
fi

mkdir $staticpath  

echo "Build angular? [y/Y]"
read ans

if [ -z $and ]
then
    authfrontpath="frontend/auth-server-front/"
    if [ $ans == "y" ] || [ $ans == "Y" ]
    then
        (
        cd $authfrontpath; 
        ng build --prod;
        )
    fi


    (
    cd $authfrontpath; 
    cp -r dist/auth-service-web/* ../../src/main/resources/static/;
    )
fi

mvn clean package 

mvnpkg=$?

rm -r src/main/resources/static/  

if [ ! $mvnpkg -eq 0 ] 
then
    exit -1
fi

# ssh -l zhuangyongj 192.168.10.128 "/home/zhuangyongj/exec/authserverweb/kill.sh"

scp "target/auth-service-web-1.0.0.jar" "zhuangyongj@192.168.10.128:~/services/auth-service-web/auth-service-web-1.0.0.jar"
