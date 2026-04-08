# Spring Boot 3.0 Cookbook - Chapter 3 - recipe3-2

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.12
    ./mvnw clean install -U
    ./mvnw spring-boot:run

    curl http://localhost:8080/actuator/health
    curl http://localhost:8080/actuator/env
    curl http://localhost:8080/actuator/metrics
    curl http://localhost:8080/actuator/beans
    curl http://localhost:8080/actuator/loggers
