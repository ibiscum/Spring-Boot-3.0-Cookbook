# Spring Boot 3.0 Cookbook - Chapter 6 - recipe6-7

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.15
    ./mvnw spring-boot:run

    yq eval -o=properties application.yml > application.properties

    docker exec -it spring-boot-cookbook-cassandra-1 cqlsh -e "CREATE KEYSPACE footballKeyspace WITH replication = {'class': 'SimpleStrategy'};"
