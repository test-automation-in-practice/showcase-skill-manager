# Skill Manager

![CI Build](https://github.com/nt-ca-aqe/skill-manager/workflows/CI%20Build/badge.svg)

## Operation Requirements

- Elasticsearch
- RabbitMQ
- PostgreSQL

## Build & Execute Service Runtime

```
./gradlew clean
./gradlew :runtime:build

docker-compose up
java -jar runtime/build/libs/runtime.jar
```

## Runtime Profiles

There are multiple application profiles available. Each will either add optional
behaviour or change the behaviour of different aspects of the application:

- `unsecured` - disables authentication requirements for all endpoints
- `with-test-data` - will create a small test data set after application launch 
