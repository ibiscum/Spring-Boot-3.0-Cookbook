package com.packt.football.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.packt.football.config.FootballContainersConfig;
import com.packt.football.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(FootballContainersConfig.class)
class UsersServiceIntegrationTest {

    @Autowired
    private UsersService usersService;

    @Test
    void createUserAgainstFlywaySchema() {
        // Act: Creates a user on the table managed by Flyway
        User user = usersService.createUser("test-flyway");

        // Assert
        assertThat(user, notNullValue());
        assertThat(user.id(), notNullValue());
    }
}

