#!/bin/bash

mvn clean package

ssh -l zhuangyongj 192.168.10.128 "/home/zhuangyongj/exec/authserver/kill-as.sh"

scp "target/auth-service-0.0.1.jar" "zhuangyongj@192.168.10.128:~/exec/authserver/authserver.jar"
