# Spring Boot 3.0 Cookbook - Chapter 1 - recipe1-7

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.12
    ./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

    curl http://localhost:8081/albums/players
