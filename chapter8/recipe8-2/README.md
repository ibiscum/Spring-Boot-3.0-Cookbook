# Spring Boot 3.0 Cookbook - Chapter 8 - recipe8-2

    mvn -N wrapper:wrapper -Dmaven=3.9.15
    ./mvnw dependency:analyze
    ./mvnw spring-boot:run

    curl http://localhost:8080/cards

    yq eval -o=properties application.yml > application.properties
