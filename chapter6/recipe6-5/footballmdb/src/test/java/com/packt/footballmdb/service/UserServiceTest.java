package com.packt.footballmdb.service;

import com.packt.footballmdb.repository.Card;
import com.packt.footballmdb.repository.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
class UserServiceTest {

    private static final String MONGO_USER = "football";
    private static final String MONGO_PASSWORD = "football";
    private static final String AUTH_DB = "admin";

    private static String[] buildMongoEvalCommand(final String command) {
        return new String[]{
                "mongosh",
                "--eval",
                command
        };
    }

    static Network mongoDbNetwork = Network.newNetwork();

    static GenericContainer<?> mongoDBContainer1 = new GenericContainer<>("mongo:latest")
            .withNetwork(mongoDbNetwork)
            .withNetworkAliases("mongo1")
            .withCommand("sh", "-c", "chmod 600 /etc/mongo/keyfile && exec mongod --replSet rs0 --port 27017 --bind_ip_all --auth --keyFile /etc/mongo/keyfile")
            .withExposedPorts(27017)
            .waitingFor(Wait.forListeningPort())
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/mongo-keyfile"), "/etc/mongo/keyfile")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/teams.json"), "teams.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/players.json"), "players.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/matches.json"), "matches.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/match_events.json"), "match_events.json");

    static GenericContainer<?> mongoDBContainer2 = new GenericContainer<>("mongo:latest")
            .withNetwork(mongoDbNetwork)
            .withNetworkAliases("mongo2")
            .withCommand("sh", "-c", "chmod 600 /etc/mongo/keyfile && exec mongod --replSet rs0 --port 27017 --bind_ip_all --auth --keyFile /etc/mongo/keyfile")
            .withExposedPorts(27017)
            .waitingFor(Wait.forListeningPort())
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/mongo-keyfile"), "/etc/mongo/keyfile");

    static GenericContainer<?> mongoDBContainer3 = new GenericContainer<>("mongo:latest")
            .withNetwork(mongoDbNetwork)
            .withNetworkAliases("mongo3")
            .withCommand("sh", "-c", "chmod 600 /etc/mongo/keyfile && exec mongod --replSet rs0 --port 27017 --bind_ip_all --auth --keyFile /etc/mongo/keyfile")
            .withExposedPorts(27017)
            .waitingFor(Wait.forListeningPort())
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/mongo-keyfile"), "/etc/mongo/keyfile");

    @BeforeAll
    static void startContainer() throws IOException, InterruptedException {
        mongoDBContainer1.start();
        mongoDBContainer2.dependsOn(mongoDBContainer1).start();
        mongoDBContainer3.dependsOn(mongoDBContainer2).start();

        String initCluster = "rs.initiate({\n"
                + " _id: \"rs0\",\n"
                + " members: [\n"
                + "   {_id: 0, host: \"mongo1:27017\"},\n"
                + "   {_id: 1, host: \"mongo2:27017\"},\n"
                + "   {_id: 2, host: \"mongo3:27017\"}\n"
                + " ]\n"
                + "})";

        for (int i = 0; i < 10; i++) {
            Container.ExecResult res = mongoDBContainer1.execInContainer(buildMongoEvalCommand(initCluster));
            if (res.getExitCode() == 0) {
                break;
            }
            Thread.sleep(1000);
        }

        for (int i = 0; i < 10; i++) {
            Container.ExecResult res = mongoDBContainer1.execInContainer(buildMongoEvalCommand("rs.status().ok"));
            if (res.getExitCode() == 0 && res.getStdout().trim().contains("1")) {
                break;
            }
            Thread.sleep(1000);
        }

        String createUser = String.format(
                "db.getSiblingDB('%s').createUser({user: '%s', pwd: '%s', roles: [{role: 'root', db: '%s'}]})",
                AUTH_DB, MONGO_USER, MONGO_PASSWORD, AUTH_DB);
        for (int i = 0; i < 10; i++) {
            Container.ExecResult res = mongoDBContainer1.execInContainer(buildMongoEvalCommand(createUser));
            if (res.getExitCode() == 0) {
                break;
            }
            Thread.sleep(1000);
        }

        importFile(mongoDBContainer1, "matches");
        importFile(mongoDBContainer1, "match_events");
        importFile(mongoDBContainer1, "teams");
        importFile(mongoDBContainer1, "players");
    }

    static void importFile(GenericContainer<?> container, String fileName) throws IOException, InterruptedException {
        String uri = String.format(
                "mongodb://%s:%s@mongo1:27017,mongo2:27017,mongo3:27017/football?replicaSet=rs0&authSource=%s&authMechanism=SCRAM-SHA-256",
                MONGO_USER,
                MONGO_PASSWORD,
                AUTH_DB);
        Container.ExecResult res = container.execInContainer(
                "mongoimport",
                "--uri=" + uri,
                "--db=football",
                "--collection=" + fileName,
                "--jsonArray",
                fileName + ".json");
        if (res.getExitCode() > 0) {
            throw new RuntimeException("MongoDB not properly initialized: " + res.getStderr());
        }
    }

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> String.format(
                "mongodb://%s:%s@%s:%d/football?authSource=%s&authMechanism=SCRAM-SHA-256&directConnection=true",
                MONGO_USER,
                MONGO_PASSWORD,
                mongoDBContainer1.getHost(),
                mongoDBContainer1.getMappedPort(27017),
                AUTH_DB));
    }

    @Autowired
    private UserService userService;

    @Test
    void buyTokens() {
        // ARRANGE
        User user = new User();
        user.setUsername("Sample user");
        User createdUser = userService.createUser(user);
        // ACT
        Integer numModified = userService.buyTokens(createdUser.getId(), 10);
        // ASSERT
        assertThat(numModified, is(1));
    }

    @Test
    void buyCards() {
        // ARRANGE
        User user = new User();
        user.setUsername("Sample user");
        User createdUser = userService.createUser(user);
        Integer buyTokens = 10;
        userService.buyTokens(createdUser.getId(), buyTokens);
        Integer requestedCards = 1;
        // ACT
        Integer cardCount = userService.buyCards(user.getId(), requestedCards);
        // ASSERT
        assertThat(cardCount, is(requestedCards));
        List<Card> cards = userService.getUserCards(user.getId());
        assertThat(cards, not(empty()));
        assertThat(cards, hasSize(requestedCards));
        Optional<User> updatedUser = userService.getUser(user.getId());
        assertThat(updatedUser.isPresent(), is(true) );
        assertThat(updatedUser.get().getTokens(), is(buyTokens-requestedCards));

    }

    @Test
    void buyCards_notEnoughTokens() {
        // ARRANGE
        User user = new User();
        user.setUsername("Sample user");
        User createdUser = userService.createUser(user);
        Integer buyTokens = 10;
        userService.buyTokens(createdUser.getId(), 10);
        Integer requestedCards = buyTokens + 1;
        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> userService.buyCards(user.getId(), requestedCards));
        Optional<User> actualUser = userService.getUser(createdUser.getId());
        assertThat(actualUser, notNullValue());
        assertThat(actualUser.get().getTokens(), is(buyTokens));
        List<Card> cards = userService.getUserCards(createdUser.getId());
        assertThat(cards, is(empty()));


    }

    @Test
    void createUser() {
        // ARRANGE
        User user = new User();
        user.setUsername("Sample user");
        // ACT
        User createdUser = userService.createUser(user);
        // ASSERT
        assertThat(createdUser, notNullValue());
        assertThat(createdUser.getId(), notNullValue());
    }
}
