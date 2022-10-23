# auth-service V1.1.4.3

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

Task scheduling in this app is supported by `Quartz` and `distributed-task-module`. Two task implementation beans are already written for this application, you may create two records in table `task` as follows to use it: 

The task implementation beans: 

- com.curtisnewbie.service.auth.infrastructure.job.GenerateUserNoJob
- com.curtisnewbie.service.auth.job.MoveOperateLogHistoryJob

See the `task` table DDL, and the insert statements in `script.sql` in resources folder:

```sql
INSERT INTO `task` (`job_name`, `target_bean`, `cron_expr`, `app_group`, `last_run_start_time`, `last_run_end_time`, `last_run_by`, `last_run_result`, `enabled`, `concurrent_enabled`, `update_date`, `update_by`)
VALUES ('OperateLogHistoryJob','moveOperateLogHistoryJob','0 0 /6 ? * *','auth-service',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'','',0,0,CURRENT_TIMESTAMP,''),
('GenerateUserNoJob','generateUserNoJob','0 0 0 ? * *','auth-service',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'','',0,0,CURRENT_TIMESTAMP,'');

```

## Modules and Dependencies

This project depends on the following modules that you must manually install (using `mvn clean install`).

- [curtisnewbie-bom](https://github.com/CurtisNewbie/curtisnewbie-bom)
- [jwt-module v1.0.1](https://github.com/CurtisNewbie/jwt-module/tree/v1.0.1)
- [distributed-task-module v2.1.1.2](https://github.com/CurtisNewbie/distributed-task-module/tree/v2.1.1.2)
- [messaging-module v2.0.7](https://github.com/CurtisNewbie/messaging-module/tree/v2.0.7)
- [common-module v2.1.9](https://github.com/CurtisNewbie/common-module/tree/v2.1.9)
