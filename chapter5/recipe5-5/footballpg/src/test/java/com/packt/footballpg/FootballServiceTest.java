package com.packt.footballpg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import com.packt.footballpg.config.FootballContainersConfig;

@SpringBootTest
@Import(FootballContainersConfig.class)
public class FootballServiceTest {

    @Autowired
    FootballService footballService;

    @Test
    public void createTeamTest() {
        // ACT
        Team team = footballService.createTeam("Jamaica");
        // ASSERT
        assertThat(team, notNullValue());
        Team team2 = footballService.getTeam(team.id());
        assertThat(team2, notNullValue());
        assertThat(team2, is(team));
    }

    @Test
    public void getTeamsTest() {
        // ARRANGE: Create a team
        Team team = footballService.createTeam("Jamaica");
        // ACT&ASSERT: Get the team
        assertThat(footballService.getTeam(team.id()), notNullValue());
    }

    @Test
    public void getTeam_notFound() {
        // ACT&ASSERT: Get a team that does not exist
        assertThat(footballService.getTeam(9999999), nullValue());
    }

}
