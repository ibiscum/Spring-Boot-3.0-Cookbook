# Spring Boot 3.0 Cookbook - Chapter 6 - recipe6-1

    ./mvnw -N wrapper:wrapper -Dmaven=3.9.15
    ./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

    mongosh "mongodb://packt:packt@localhost:27017/football?authSource=admin"
    mongoimport "mongodb://packt:packt@localhost:27017/football?authSource=admin" --collection "teams" --file "teams.json" --jsonArray

