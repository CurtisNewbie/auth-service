# auth-service

Service for managing users, access log and operation log, it internally uses Dubbo RPC framework, and the API layer is under **`/auth-service-remote`**.

## Middlewares

- MySQL
- Nacos (or others, e.g., zookeeper)

## Modules and Dependencies

This project depends on the following modules that you must manually install (using `mvn clean install`).

- curtisnewbie-bom
    - description: BOM file for dependency management
    - url: https://github.com/CurtisNewbie/curtisnewbie-bom
    - branch: main
    - version: micro-0.0.1 (under `/microservce` folder)

- service-module
    - description: import dependencies for a Dubbo service
    - url: https://github.com/CurtisNewbie/service-module
    - branch: main

- log-tracing-module
    - desription: for log tracing between web endpoints and service layers
    - url: https://github.com/CurtisNewbie/log-tracing-module
    - branch: main


## Projects that uses this service (examples)

1. FileServer
    - url: https://github.com/CurtisNewbie/file-server/
    - branch: main 
