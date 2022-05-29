#!/bin/bash

remote="alphaboi@curtisnewbie.com"
remote_path="/home/alphaboi/services/nginx/html/"

# build angular
authfrontpath="frontend/auth-server-front/"
(
cd $authfrontpath; 
ng build --prod;
)

scp -r "${authfrontpath}/dist/auth-service-web/" "${remote}:${remote_path}"


