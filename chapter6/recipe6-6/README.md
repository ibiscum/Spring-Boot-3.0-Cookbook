# Spring Boot 3.0 Cookbook - Chapter 6 - recipe6-6

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.15
    ./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

    yq eval -o=properties application.yml > application.properties
