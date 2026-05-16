package com.packt.footballobs.health;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class FootballHealthIndicator implements HealthIndicator {

    // Best Practice für Spring Boot 4: Verwendung des neuen, flüssigen JdbcClient
    private final JdbcClient jdbcClient;

    public FootballHealthIndicator(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Health health() {
        try {
            // Führt die SQL-Abfrage typsicher und flüssig aus
            jdbcClient.sql("SELECT 1").query().singleRow();
            return Health.up().build();
        } catch (DataAccessException e) {
            // Gibt die Fehlermeldung strukturiert an das Actuator-Framework weiter
            return Health.down()
                    .withDetail("error", "Cannot connect to database")
                    .withDetail("message", e.getMessage())
                    .build();
        }
    }
}

