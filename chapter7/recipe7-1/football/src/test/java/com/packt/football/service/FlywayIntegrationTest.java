package com.packt.football.service;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import com.packt.football.config.FootballContainersConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(FootballContainersConfig.class)
class FlywayIntegrationTest {

    @Autowired
    private Flyway flyway;

    @Test
    void verifyFlywayHasInitializedDatabase() {
        // 1. Prüfen, ob Flyway Bean geladen wurde
        assertThat(flyway).isNotNull();

        // 2. Prüfen, ob ausstehende Migrationen vorhanden sind (Sollte 0 sein, wenn voll initialisiert)
        var migrationInfoService = flyway.info();
        assertThat(migrationInfoService.pending()).isEmpty();

        // 3. Prüfen, ob mindestens eine Migration erfolgreich angewendet wurde
        assertThat(migrationInfoService.applied()).isNotEmpty();

        // Optional: Konsolen-Output aller Migrationen für das Log-Review
        for (var migration : migrationInfoService.all()) {
            System.out.printf("Migration: %s, Status: %s%n",
                migration.getScript(), migration.getState());
        }
    }
}

