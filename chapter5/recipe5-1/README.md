# Spring Boot 3.0 Cookbook - Chapter 5 - recipe5-1

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.15
    ./mvnw clean install -U
    ./mvnw spring-boot:run

    curl http://localhost:8080/actuator/health
    curl http://localhost:8080/actuator/env
    curl http://localhost:8080/actuator/metrics
    curl http://localhost:8080/actuator/beans
    curl http://localhost:8080/actuator/loggers
