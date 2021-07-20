# auth-service

Service that handles authentication-related operations, it internally uses Dubbo RPC framework.

## Dependencies

This project depends on the following modules that you must manually install (using `mvn clean install`).

### 1. service-module

Make the app a standalone Dubbo service

```
URL: https://github.com/CurtisNewbie/service-module
Branch: main
```

## Projects that uses this service (examples)

1. FileServer

```
URL: https://github.com/CurtisNewbie/file-server/tree/microservice
Branch: microservice
```
