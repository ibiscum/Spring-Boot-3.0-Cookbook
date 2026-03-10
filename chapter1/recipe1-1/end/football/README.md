# Spring Boot 3.0 Cookbook - Chapter 1 - recipe1-1

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.12
    ./mvnw spring-boot:run

    curl http://localhost:8080/players
    curl http://localhost:8080/players/Ivana%20ANDRES
    curl --header "Content-Type: application/text" --request POST --data 'Itana BONMATI' http://localhost:8080/players
    curl --header "Content-Type: application/text" --request PUT --data 'Aitana BONMATI' http://localhost:8080/players/Itana%20BONMATI
    curl --header "Content-Type: application/text" --request DELETE http://localhost:8080/players/Aitana%20BONMATI
