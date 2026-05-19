# Spring Boot 3.0 Cookbook - Chapter 3 - recipe3-4

    docker compose up

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.12
    ./mvnw clean install -U
    ./mvnw spring-boot:run
    ./mvnw dependency:tree


    watch curl http://localhost:8090/players

## Zipkin

    http://localhost:9411/

## OTEL collector

    curl http://localhost:4318/v1/traces -H "Content-Type: application/json" -d '{"resourceSpans":[]}'
