# auth-service

Service for managing users, authentication, access log, operation log and so on.

***Do not run the 'build' scripts, these are written for my development environment only***

***The frontend project has been moved to a new repository [auth-service-front](https://github.com/CurtisNewbie/auth-service-front)***

## Middleware

- MySQL
- Nacos
- RabbitMQ
- Redis

## Task Scheduling  

Task scheduling in this app is supported by `Quartz` and `distributed-task-module`. Two task implementation beans are already written for this application, you may create two records in table `task` as follows to use it: 

The task implementation beans: 

- com.curtisnewbie.service.auth.job.MoveAccessLogHistoryJob
- com.curtisnewbie.service.auth.job.MoveOperateLogHistoryJob

In table `task`:

|id |job_name      |target_bean |cron_expr    |app_group   |enabled|concurrent_enabled|
|---|--------------|------------|-------------|------------|-------|------------------|
|1  |AccessLogHistoryJob |moveAccessLogHistoryJob |0 0 1 ? * *|auth-service|1     |0 |
|2  |OperateLogHistoryJob|moveOperateLogHistoryJob|0 0 1 ? * *|auth-service|1     |0|

## Modules and Dependencies

This project depends on the following modules that you must manually install (using `mvn clean install`).

- curtisnewbie-bom
    - description: BOM file for dependency management
    - url: https://github.com/CurtisNewbie/curtisnewbie-bom
    - under `/microservice` folder

- jwt-module
    - description: module for JWT encoding / decoding 
    - url: https://github.com/CurtisNewbie/jwt-module

- service-module
    - description: import dependencies for a Dubbo service
    - url: https://github.com/CurtisNewbie/service-module

- messaging-module
    - description: for RabbitMQ-based messaging 
    - url: https://github.com/CurtisNewbie/messaging-module

- distributed-task-module
    - description: for distributed task scheduling
    - url: https://github.com/CurtisNewbie/distributed-task-module

## Projects that uses this service (examples)

1. file-server 
    - url: https://github.com/CurtisNewbie/file-server

2. auth-gateway 
    - url: https://github.com/CurtisNewbie/auth-gateway
