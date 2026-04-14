# Spring Boot 3.0 Cookbook - Chapter 3 - recipe3-3

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.12
    ./mvnw clean install -U
    ./mvnw spring-boot:run

    curl http://localhost:8080/actuator/health
    watch curl http://localhost:8080/actuator/health/readiness
    watch -n 1 -x curl --request POST -H "Content-Type: application/json" --data "1" http://localhost:8080/football
    watch curl http://localhost:8080/actuator/health
