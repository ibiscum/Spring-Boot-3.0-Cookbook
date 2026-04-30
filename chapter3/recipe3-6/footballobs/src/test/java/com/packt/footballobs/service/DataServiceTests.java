package com.packt.footballobs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class DataServiceTests {

    @Test
    void getPlayerStatsExecutesJdbcSleepAndReturnsSummary() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        DataService dataService = new DataService(jdbcTemplate);

        String stats = dataService.getPlayerStats("Aitana Bonmatí");

        assertThat(stats).isEqualTo("some complex stats for player Aitana Bonmatí");
        verify(jdbcTemplate).execute(startsWith("SELECT pg_sleep("));
    }
}
