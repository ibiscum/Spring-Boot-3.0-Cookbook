# Spring Boot 3.0 Cookbook - Chapter 2 - recipe2-1

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.12
    ./mvnw clean install -U
    ./mvnw spring-boot:run

    curl http://localhost:9000/.well-known/openid-configuration
    curl -v "http://localhost:9000/oauth2/token" --header "Content-Type: application/x-www-form-urlencoded" -d "grant_type=client_credentials" -d "client_id=football" -d "client_secret=SuperSecret" -d "scope=football:read"
