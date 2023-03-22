# auth-service V1.1.6

Service for managing users, authentication, access log, operation log and so on.

***Do not run the 'build' scripts, these are written for my development environment only***

***The frontend project has been moved to a new repository [auth-service-front](https://github.com/CurtisNewbie/auth-service-front)***

## Requirements 

- auth-service-front (Angular frontend) >= [v1.1.7](https://github.com/CurtisNewbie/auth-service-front/tree/v1.1.7)
- goauth >= [v1.0.0](https://github.com/CurtisNewbie/goauth/tree/v1.0.0)
- MySQL 5.7 or 8
- Consul
- RabbitMQ
- Redis
- Java 11

## Task Scheduling  

Task scheduling in this app is supported by `Quartz` and `distributed-task-module`. A few task implementation beans are already written for this application, these tasks are automatically registered and will run on application startup: 

The task implementation beans: 

- com.curtisnewbie.service.auth.infrastructure.job.GenerateUserNoJob
- com.curtisnewbie.service.auth.job.MoveOperateLogHistoryJob

## Modules and Dependencies

This project depends on the following modules that you must manually install (using `mvn clean install`).

- [curtisnewbie-bom](https://github.com/CurtisNewbie/curtisnewbie-bom)
- [jwt-module v1.0.1](https://github.com/CurtisNewbie/jwt-module/tree/v1.0.1)
- [distributed-task-module v2.1.1.4](https://github.com/CurtisNewbie/distributed-task-module/tree/v2.1.1.4)
- [messaging-module v2.0.8](https://github.com/CurtisNewbie/messaging-module/tree/v2.0.8)
- [common-module v2.2.0](https://github.com/CurtisNewbie/common-module/tree/v2.2.0)
- [goauth-client-java v1.0.0](https://github.com/CurtisNewbie/goauth/tree/v1.0.0/client/goauth-client-java)

## Updates

- Since v1.1.6, `goauth` is required for path level authorization and resource management. Roles are no longer hard-coded, instead, role_no specify the role that the user has, and that role can be bound to 1..N resources, depending on what the role is allowed to access. The role and resource management is handled by `goauth` service.

- Since release V1.1.4.5, LocalDateTime / Date are serialized and deserialized in the form of epoch time (milliseconds), this is not backward compatible, some MQ messages may also be affected as well.
