package com.packt.football.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class FootballContainersConfig {

    @Bean
    @ServiceConnection
    @SuppressWarnings("resource")
    public PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"))
                .withDatabaseName("football")
                .withUsername("packt")
                .withPassword("your_secure_password");
                // Lädt Ihr SQL-Skript automatisch aus src/test/resources/init-scripts/01-init.sql
                // .withInitScript("init-scripts/01-init.sql");
    }
}
