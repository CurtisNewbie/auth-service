# auth-service V1.1.4.4

Service for managing users, authentication, access log, operation log and so on.

***Do not run the 'build' scripts, these are written for my development environment only***

***The frontend project has been moved to a new repository [auth-service-front](https://github.com/CurtisNewbie/auth-service-front)***

## Requirements 

- auth-service-front (Angular frontend) >= [v1.1.5](https://github.com/CurtisNewbie/auth-service-front/tree/v1.1.5)
- MySQL 5.7 or 8
- Consul
- RabbitMQ
- Redis

## Task Scheduling  

Task scheduling in this app is supported by `Quartz` and `distributed-task-module`. A few task implementation beans are already written for this application, these tasks are automatically registered and will run on application startup: 

The task implementation beans: 

- com.curtisnewbie.service.auth.infrastructure.job.GenerateUserNoJob
- com.curtisnewbie.service.auth.job.MoveOperateLogHistoryJob

## Modules and Dependencies

This project depends on the following modules that you must manually install (using `mvn clean install`).

- [curtisnewbie-bom](https://github.com/CurtisNewbie/curtisnewbie-bom)
- [jwt-module v1.0.1](https://github.com/CurtisNewbie/jwt-module/tree/v1.0.1)
- [distributed-task-module v2.1.1.3](https://github.com/CurtisNewbie/distributed-task-module/tree/v2.1.1.3)
- [messaging-module v2.0.7](https://github.com/CurtisNewbie/messaging-module/tree/v2.0.7)
- [common-module v2.1.9](https://github.com/CurtisNewbie/common-module/tree/v2.1.9)
