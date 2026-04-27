# Spring Boot 3.0 Cookbook - Chapter 2 - recipe2-4

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.15
    ./mvnw clean install -U
    ./mvnw spring-boot:build-image -Dspring-boot.build-image.cleanCache=true -Dspring-boot.build-image.docker.host=unix:///run/user/1000/docker.sock -Dspring-boot.build-image.docker.bindHostToBuilder=true

    ./mvnw spring-boot:run
    ./mvnw test -Dspring.profiles.active=test

    curl -v http://localhost:8080/football/teams
    curl -v http://localhost:9000/oauth2/token --H "Content-Type: application/x-www-form-urlencoded" -d "grant_type=client_credentials" -d "client_id=football" -d "client_secret=SuperSecret" -d "scope=football:read"
    curl -H "Authorization: Bearer <access_token>" http://localhost:8080/football/teams
