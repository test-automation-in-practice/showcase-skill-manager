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
