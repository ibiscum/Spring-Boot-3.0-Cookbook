# Spring Boot 3.0 Cookbook - Chapter 4 - recipe4-2

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.12
    ./mvnw clean install -U
    ./mvnw spring-boot:run

    docker run --rm -i hadolint/hadolint < Dockerfile
    docker run -t --rm -v ${PWD}:/app zavoloklom/dclint .
    docker build -t recipe1-1 .
    docker run -p 8080:8080 recipe1-1

    curl http://localhost:8080/players
    curl http://localhost:8080/players/Ivana%20ANDRES
    curl --header "Content-Type: application/text" --request POST --data 'Itana BONMATI' http://localhost:8080/players
    curl --header "Content-Type: application/text" --request PUT --data 'Aitana BONMATI' http://localhost:8080/players/Itana%20BONMATI
    curl --header "Content-Type: application/text" --request DELETE http://localhost:8080/players/Aitana%20BONMATI
