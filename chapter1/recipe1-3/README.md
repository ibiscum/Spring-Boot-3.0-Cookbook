# Spring Boot 3.0 Cookbook - Chapter 1 - recipe1-3

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.12
    ./mvnw clean install -U
    ./mvnw spring-boot:run

    curl http://localhost:8080/players
    curl http://localhost:8080/players/99999

    curl --header "Content-Type: application/json" --request POST --data '{"id": 8888, "jerseyNumber": 6, "name": "Cata COLL", "position": "Goalkeeper", "dateOfBirth": "2001-04-23"}' http://localhost:8080/players
