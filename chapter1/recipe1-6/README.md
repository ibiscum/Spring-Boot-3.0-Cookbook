# Spring Boot 3.0 Cookbook - Chapter 1 - recipe1-6

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.15
    ./mvnw clean install -U
    ./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

    curl http://localhost:8081/albums/players
