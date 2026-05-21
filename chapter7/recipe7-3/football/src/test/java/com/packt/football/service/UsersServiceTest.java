package com.packt.football.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.packt.football.config.FootballContainersConfig;
import com.packt.football.domain.User;

@SpringBootTest
@Import(FootballContainersConfig.class)
class UsersServiceTest {

    @Autowired
    UsersService usersService;

    @Test
    void createUser() {
        User user1 = usersService.createUser("test");
        assertThat(user1, notNullValue());
        assertThat(user1.id(), notNullValue());
    }

    @Test
    void getUsers() {
        User user1 = usersService.createUser("test");

        List<User> users = usersService.getUsers();
        assertThat(users, not(empty()));
        assertThat(users, hasItem(user1));
    }

    @Test
    void getUser() {
        User user1 = usersService.createUser("test");

        User userRetrieved = usersService.getUser(user1.id());
        assertThat(userRetrieved, notNullValue());
        assertThat(userRetrieved, is(user1));
    }
}
