# auth-service

Service for managing users, access log and operation log, it internally uses Dubbo RPC framework. The API layer for Dubbo is under **`/auth-service-remote`**, and the exposed routing information for messaging/MQ is under **`/auth-service-messaging`**.

***Do not run the 'build' scripts, these are written for my development environment only***

## Middleware

- MySQL
- Nacos (or others, e.g., zookeeper)
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
    - branch: main
    - under `/microservice` folder

- service-module
    - description: import dependencies for a Dubbo service
    - url: https://github.com/CurtisNewbie/service-module
    - branch: main

- log-tracing-module
    - description: for log tracing between web endpoints and service layers
    - url: https://github.com/CurtisNewbie/log-tracing-module
    - branch: main

- messaging-module
    - description: for RabbitMQ-based messaging 
    - url: https://github.com/CurtisNewbie/messaging-module
    - branch: main

- auth-module
    - description: for user authentication, security and integration with auth-service (this is used by `auth-service-web`) 
    - url: https://github.com/CurtisNewbie/auth-module
    - branch: main 

- distributed-task-module
    - description: for distributed task scheduling
    - url: https://github.com/CurtisNewbie/distributed-task-module
    - branch: main

## Projects that uses this service (examples)

1. FileServer
    - url: https://github.com/CurtisNewbie/file-server/
    - branch: main 
