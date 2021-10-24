#!/bin/bash

remoteaddr="curtisnewbie.com"

# build angular
authfrontpath="frontend/auth-server-front/"
(
cd $authfrontpath; 
ng build --prod;
)

scp -r "${authfrontpath}/dist/auth-service-web/" "zhuangyongj@${remoteaddr}:/home/zhuangyongj/services/nginx/html/auth-service-web/"


