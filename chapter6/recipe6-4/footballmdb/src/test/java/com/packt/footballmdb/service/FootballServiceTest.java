package com.packt.footballmdb.service;

import com.packt.footballmdb.repository.Player;
import com.packt.footballmdb.repository.Team;

import org.junit.jupiter.api.Test;
import org.testcontainers.mongodb.MongoDBContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Testcontainers
class FootballServiceTest {

    private static final String MONGO_USER = "football";
    private static final String MONGO_PASSWORD = "football";
    private static final String AUTH_DB = "admin";

    @SuppressWarnings("resource")
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo")
            .withEnv("MONGO_INITDB_ROOT_USERNAME", MONGO_USER)
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", MONGO_PASSWORD)
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/teams.json"), "teams.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/players.json"), "players.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/matches.json"), "matches.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/match_events.json"), "match_events.json");

    static void importFile(String fileName) throws IOException, InterruptedException {
        String uri = "mongodb://" + MONGO_USER + ":" + MONGO_PASSWORD + "@127.0.0.1:27017/?directConnection=true&authSource=" + AUTH_DB + "&authMechanism=SCRAM-SHA-256";
        org.testcontainers.containers.Container.ExecResult res = null;

        for (int attempt = 1; attempt <= 10; attempt++) {
            res = mongoDBContainer.execInContainer(
                    "mongoimport",
                    "--uri=" + uri,
                    "--db=football",
                    "--collection=" + fileName,
                    "--jsonArray",
                    fileName + ".json");
            if (res.getExitCode() == 0) {
                return;
            }
            if (attempt < 10) {
                Thread.sleep(1000);
            }
        }

        throw new RuntimeException("MongoDB not properly initialized: " + (res != null ? res.getStderr() : "unknown error"));
    }

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> "mongodb://" + MONGO_USER + ":" + MONGO_PASSWORD + "@"
                + mongoDBContainer.getHost() + ":" + mongoDBContainer.getMappedPort(27017)
                + "/football?directConnection=true&authSource=" + AUTH_DB);
    }

    @Autowired
    private FootballService footballService;

    @Test
    void getTeam() {
        Team team = footballService.getTeam("1884881");
        assertNotNull(team);
    }

    @Test
    void getTeam_notExists() {
        Team team = footballService.getTeam("99999999");
        assertNull(team);
    }

    @Test
    void getTeamByName() {
        Team team = footballService.getTeamByName("Argentina");
        assertNotNull(team);
    }

    @Test
    void getTeamsContainingName() {
        List<Team> teams = footballService.getTeamsContainingName("land");
        assertNotNull(teams);
        assertThat(teams, not(empty()));
        assertThat(teams, hasSize(5));
    }

    @Test
    void getPlayer() {
        Player player = footballService.getPlayer("387138");
        assertThat(player, notNullValue());
    }

    @Test
    void saveTeam() {
        // ACT
        Team t = new Team();
        t.setName("Senegal");
        Team savedTeam = footballService.saveTeam(t);
        // ASSERT
        assertThat(savedTeam, notNullValue());
        assertThat(savedTeam.getId(), notNullValue());
        Team retreivedTeam = footballService.getTeam(savedTeam.getId());
        assertThat(retreivedTeam, notNullValue());
        // CLEAN-UP
        footballService.deleteTeam(retreivedTeam.getId());
    }

    @Test
    void deleteTeam() {
        // ARRANGE
        Team t = new Team();
        t.setName("Senegal");
        Team savedTeam = footballService.saveTeam(t);
        // ACT
        footballService.deleteTeam(savedTeam.getId());
        // ASSERT
        Team deletedTeam = footballService.getTeam(savedTeam.getId());
        assertThat(deletedTeam, nullValue());
    }

    @Test
    void updateTeamName() {
        // ARRANGE
        Team t = new Team();
        t.setName("Veneçuela");
        Team savedTeam = footballService.saveTeam(t);
        // ACT
        footballService.updateTeamName(savedTeam.getId(), "Venezuela");
        // ASSERT
        Team updatedTeam = footballService.getTeam(savedTeam.getId());
        assertThat(updatedTeam.getName(), is("Venezuela"));
    }
}
