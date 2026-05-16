// package com.packt.footballobs.health;

// import org.springframework.boot.actuate.health.Health;
// import org.springframework.boot.actuate.health.HealthIndicator;
// import org.springframework.dao.DataAccessException;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.stereotype.Component;

// @Component
// public class FootballHealthIndicator implements HealthIndicator {

//     private JdbcTemplate template;

//     public FootballHealthIndicator(JdbcTemplate template) {
//         this.template = template;
//     }

//     @Override
//     public Health health() {
//         try {
//             template.execute("SELECT 1");
//             return Health.up().build();
//         } catch (DataAccessException e) {
//             return Health.down().withDetail("Cannot connect to database", e).build();
//         }

//     }

// }
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

