# Spring Boot 3.0 Cookbook - Chapter 2 - recipe2-4

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.12
    ./mvnw clean install -U
    ./mvnw spring-boot:run

    curl -v http://localhost:8080/football/teams
    curl -v http://localhost:9000/oauth2/token --H "Content-Type: application/x-www-form-urlencoded" -d "grant_type=client_credentials" -d "client_id=football" -d "client_secret=SuperSecret" -d "scope=football:read"
    curl -H "Authorization: Bearer <access_token>" http://localhost:8080/football/teams
