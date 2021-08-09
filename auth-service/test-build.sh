#!/bin/bash

mvn clean package 

scp "target/auth-service-0.0.1.jar" "zhuangyongj@192.168.10.128:~/exec/authserver.jar"
