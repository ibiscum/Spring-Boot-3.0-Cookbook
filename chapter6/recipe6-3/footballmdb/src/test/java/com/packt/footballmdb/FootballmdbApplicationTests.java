package com.packt.footballmdb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;

@SpringBootTest
@Testcontainers
class FootballmdbApplicationTests {

    private static final String MONGO_USER = "football";
    private static final String MONGO_PASSWORD = "football";
    private static final String AUTH_DB = "admin";

    @SuppressWarnings("resource")
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo")
            .withEnv("MONGO_INITDB_ROOT_USERNAME", MONGO_USER)
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", MONGO_PASSWORD);

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> "mongodb://" + MONGO_USER + ":" + MONGO_PASSWORD + "@"
                + mongoDBContainer.getHost() + ":" + mongoDBContainer.getMappedPort(27017)
                + "/football?directConnection=true&authSource=" + AUTH_DB);
    }

    @Test
    void contextLoads() {
    }

}
