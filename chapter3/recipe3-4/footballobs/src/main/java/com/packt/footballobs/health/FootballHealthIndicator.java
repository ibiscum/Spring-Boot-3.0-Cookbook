package com.packt.footballobs.health;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FootballHealthIndicator implements HealthIndicator {

    private final JdbcTemplate template;

    public FootballHealthIndicator(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Health health() {
        try {
            template.execute("SELECT 1");
            return Health.up().build();
        } catch (DataAccessException e) {
            return Health.down()
                    .withDetail("error", "Cannot connect to database")
                    .withException(e)
                    .build();
        }
    }
}
