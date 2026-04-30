package com.packt.footballclient.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.packt.footballclient.domain.PlayerRanking;

class PlayersControllerTests {

    @Test
    void getPlayersReturnsARankingForEachPlayer() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForObject(anyString(), eq(int.class))).thenReturn(42);

        RestTemplateBuilder restTemplateBuilder = mock(RestTemplateBuilder.class);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        PlayersController controller = new PlayersController(restTemplateBuilder);
        List<PlayerRanking> rankings = controller.getPlayers();

        assertThat(rankings).hasSize(23);
        assertThat(rankings).allMatch(ranking -> ranking.ranking() == 42);
        assertThat(rankings).extracting(PlayerRanking::player).contains("Aitana Bonmatí", "Martens", "Sandra Paños");

        verify(restTemplate, times(23)).getForObject(startsWith("http://localhost:8080/football/ranking/"), eq(int.class));
    }
}
