package com.packt.football.service;

import com.packt.football.config.FootballContainersConfig;
import com.packt.football.domain.*;
import com.packt.football.repo.MatchEventEntity;
import com.packt.football.repo.PlayerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

@SpringBootTest
@Import(FootballContainersConfig.class)
class DynamicQueriesServiceTest {

    @Autowired
    DynamicQueriesService dynamicQueriesService;

    @Autowired
    UsersService usersService;

    @Autowired
    AlbumsService albumsService;

    @Test
    public void searchTeamPlayersTest() {
        List<PlayerEntity> players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.of("ila"),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(players, not(empty()));

        int minHeight = players.stream().map(PlayerEntity::getHeight).min(Integer::compareTo).orElseThrow();
        int maxHeight = players.stream().map(PlayerEntity::getHeight).min(Integer::compareTo).orElseThrow();

        players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.of("3$@"), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(players, empty());

        players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.empty(), Optional.of(minHeight - 1),
                Optional.of(maxHeight + 1), Optional.empty(), Optional.empty());
        assertThat(players, not(empty()));

        players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.empty(), Optional.of(190), Optional.of(200),
                Optional.empty(), Optional.empty());
        assertThat(players, empty());

        players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(40), Optional.of(100));
        assertThat(players, not(empty()));

        players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(100), Optional.of(140));
        assertThat(players, empty());
    }

    @Test
    public void searchTeamPlayersAndMapTest() {
        List<Player> players = dynamicQueriesService.searchTeamPlayersAndMap(1884881, Optional.of("ila"),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(players, not(empty()));

        players = dynamicQueriesService.searchTeamPlayersAndMap(1884881, Optional.of("3$@"), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(players, empty());

        players = dynamicQueriesService.searchTeamPlayersAndMap(1884881, Optional.empty(), Optional.of(170),
                Optional.of(190), Optional.empty(), Optional.empty());
        assertThat(players, not(empty()));

        players = dynamicQueriesService.searchTeamPlayersAndMap(1884881, Optional.empty(), Optional.of(190),
                Optional.of(200), Optional.empty(), Optional.empty());
        assertThat(players, empty());

        players = dynamicQueriesService.searchTeamPlayersAndMap(1884881, Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(40), Optional.of(100));
        assertThat(players, not(empty()));

        players = dynamicQueriesService.searchTeamPlayersAndMap(1884881, Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(100), Optional.of(140));
        assertThat(players, empty());
    }

    @Test
    public void searchMatchEventsRangeTest() {
        List<MatchEventEntity> events = dynamicQueriesService.searchMatchEventsRange(400222854, Optional.empty(),
                Optional.empty());
        assertThat(events, not(empty()));
        assertThat(events, hasSize(227));

        events = dynamicQueriesService.searchMatchEventsRange(400222854,
                Optional.of(LocalDateTime.of(2023, 7, 21, 5, 8, 0)), Optional.empty());
        assertThat(events, not(empty()));
        assertThat(events, hasSize(201));

        events = dynamicQueriesService.searchMatchEventsRange(400222854, Optional.empty(),
                Optional.of(LocalDateTime.of(2023, 7, 21, 5, 8, 0)));
        assertThat(events, not(empty()));
        assertThat(events, hasSize(26));

        events = dynamicQueriesService.searchMatchEventsRange(400222854,
                Optional.of(LocalDateTime.of(2023, 7, 21, 5, 8, 0)),
                Optional.of(LocalDateTime.of(2023, 7, 21, 5, 10, 0)));
        assertThat(events, not(empty()));
        assertThat(events, hasSize(2));

        events = dynamicQueriesService.searchMatchEventsRange(400222854,
                Optional.of(LocalDateTime.of(2024, 8, 16, 10, 2, 0)),
                Optional.of(LocalDateTime.of(2024, 8, 16, 10, 4, 0)));
        assertThat(events, empty());
    }

    @Test
    void searchMatchEventsRangeAndMap() {
        List<MatchEvent> events = dynamicQueriesService.searchMatchEventsRangeAndMap(400222854, Optional.empty(),
                Optional.empty());
        assertThat(events, not(empty()));
        assertThat(events, hasSize(227));

        events = dynamicQueriesService.searchMatchEventsRangeAndMap(400222854,
                Optional.of(LocalDateTime.of(2023, 7, 21, 5, 8, 0)), Optional.empty());
        assertThat(events, not(empty()));
        assertThat(events, hasSize(201));

        events = dynamicQueriesService.searchMatchEventsRangeAndMap(400222854, Optional.empty(),
                Optional.of(LocalDateTime.of(2023, 7, 21, 5, 8, 0)));
        assertThat(events, not(empty()));
        assertThat(events, hasSize(26));

        events = dynamicQueriesService.searchMatchEventsRangeAndMap(400222854,
                Optional.of(LocalDateTime.of(2023, 7, 21, 5, 8, 0)),
                Optional.of(LocalDateTime.of(2023, 7, 21, 5, 10, 0)));
        assertThat(events, not(empty()));
        assertThat(events, hasSize(2));

        events = dynamicQueriesService.searchMatchEventsRangeAndMap(400222854,
                Optional.of(LocalDateTime.of(2024, 8, 16, 10, 2, 0)),
                Optional.of(LocalDateTime.of(2024, 8, 16, 10, 4, 0)));
        assertThat(events, empty());

    }

    @Test
    void deleteEventRangeTest() {
        List<MatchEventEntity> events = dynamicQueriesService.searchMatchEventsRange(400258556, Optional.empty(),
                Optional.empty());
        assertThat(events, not(empty()));
        assertThat(events, hasSize(258));

        dynamicQueriesService.deleteEventRange(400258556, LocalDateTime.of(2023, 8, 16, 10, 2, 0),
                LocalDateTime.of(2023, 8, 16, 10, 4, 0));

        events = dynamicQueriesService.searchMatchEventsRange(400258556, Optional.empty(), Optional.empty());
        assertThat(events, not(empty()));
        assertThat(events, hasSize(252));

    }

    @Test
    void searchUserMissingPlayers() {
        User user1 = this.usersService.createUser("user1");
        List<PlayerEntity> players = dynamicQueriesService.searchUserMissingPlayers(user1.id());
        assertThat(players, not(empty()));
        assertThat(players, hasSize(736));

        albumsService.buyAlbum(user1.id(), "album1");
        albumsService.buyCards(user1.id(), 1);
        albumsService.useAllCardAvailable(user1.id());
        players = dynamicQueriesService.searchUserMissingPlayers(user1.id());
        assertThat(players, hasSize(735));
    }

    @Test
    void searchUserMissingPlayersAndMap() {
        User user1 = this.usersService.createUser("user1");
        List<Player> players = dynamicQueriesService.searchUserMissingPlayersAndMap(user1.id());
        assertThat(players, not(empty()));
        assertThat(players, hasSize(736));

        albumsService.buyAlbum(user1.id(), "album1");
        albumsService.buyCards(user1.id(), 1);
        albumsService.useAllCardAvailable(user1.id());
        players = dynamicQueriesService.searchUserMissingPlayersAndMap(user1.id());
        assertThat(players, hasSize(735));
    }
}
