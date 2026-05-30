# Spring Boot 3.0 Cookbook - Chapter 7 - recipe7-6

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.12
    ./mvnw clean install -U
    ./mvnw spring-boot:run

    docker run --rm -i hadolint/hadolint < Dockerfile
    docker run -t --rm -v ${PWD}:/app zavoloklom/dclint .
