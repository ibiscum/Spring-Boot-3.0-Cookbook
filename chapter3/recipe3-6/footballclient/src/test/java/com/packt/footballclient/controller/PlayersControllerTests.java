// package com.packt.footballclient.controller;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.ArgumentMatchers.startsWith;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.util.List;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.web.client.RestTemplateBuilder;
// import org.springframework.web.client.RestTemplate;

// import com.packt.footballclient.domain.PlayerRanking;

// class PlayersControllerTests {

//     @Test
//     void getPlayersReturnsARankingForEachPlayer() {
//         RestTemplate restTemplate = mock(RestTemplate.class);
//         when(restTemplate.getForObject(anyString(), eq(int.class))).thenReturn(42);

//         RestTemplateBuilder restTemplateBuilder = mock(RestTemplateBuilder.class);
//         when(restTemplateBuilder.build()).thenReturn(restTemplate);

//         PlayersController controller = new PlayersController(restTemplateBuilder);
//         List<PlayerRanking> rankings = controller.getPlayers();

//         assertThat(rankings).hasSize(23);
//         assertThat(rankings).allMatch(ranking -> ranking.ranking() == 42);
//         assertThat(rankings).extracting(PlayerRanking::player).contains("Aitana Bonmatí", "Martens", "Sandra Paños");

//         verify(restTemplate, times(23)).getForObject(startsWith("http://localhost:8080/football/ranking/"), eq(int.class));
//     }
// }

package com.packt.footballclient.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import com.packt.footballclient.domain.PlayerRanking;

class PlayersControllerTests {

    @Test
    void getPlayersReturnsARankingForEachPlayer() {
        // 1. Fluent-API über DEEP_STUBS kompakt mocken
        RestClient restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);
        RestClient.Builder restClientBuilder = mock(RestClient.Builder.class);

        // 2. Den Builder simulieren, der sich selbst zurückgibt und am Ende den Client liefert
        when(restClientBuilder.baseUrl(anyString())).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);

        // 3. Den verschachtelten Aufruf .get().uri(..).retrieve().body(Integer.class) simulieren
        when(restClient.get()
                .uri(eq("/{player}"), any(Object[].class))
                .retrieve()
                .body(eq(Integer.class)))
                .thenReturn(42);

        // 4. Controller instanziieren und Methode ausführen
        PlayersController controller = new PlayersController(restClientBuilder);
        List<PlayerRanking> rankings = controller.getPlayers();

        // 5. Assertions prüfen die Fachlogik
        assertThat(rankings).hasSize(23);
        assertThat(rankings).allMatch(ranking -> ranking.ranking() == 42);
        assertThat(rankings).extracting(PlayerRanking::player).contains("Aitana Bonmatí", "Martens", "Sandra Paños");

        // 6. Verifikation: Prüfen, ob die exakte URI-Template-Struktur 23-mal aufgerufen wurde
        verify(restClient.get(), times(23)).uri(eq("/{player}"), any(Object[].class));
    }
}
