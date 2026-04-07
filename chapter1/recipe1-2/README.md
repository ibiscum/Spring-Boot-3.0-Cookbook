# Spring Boot 3.0 Cookbook - Chapter 1 - recipe1-2

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.12
    ./mvnw clean install -U
    ./mvnw spring-boot:run

    docker run --rm -i hadolint/hadolint < Dockerfile
    docker run -t --rm -v ${PWD}:/app zavoloklom/dclint .
    docker build -t recipe1-2 .
    docker run -p 8080:8080 recipe1-2

    curl http://localhost:8080/players
    curl http://localhost:8080/players/396929
