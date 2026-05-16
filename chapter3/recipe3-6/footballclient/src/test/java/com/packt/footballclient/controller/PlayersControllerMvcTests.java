// package com.packt.footballclient.controller;

// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.web.client.RestTemplateBuilder;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import org.springframework.web.client.RestTemplate;

// class PlayersControllerMvcTests {

//     private MockMvc mockMvc;

//     private RestTemplateBuilder restTemplateBuilder;
//     private RestTemplate restTemplate;

//     @BeforeEach
//     void setUp() {
//         restTemplate = org.mockito.Mockito.mock(RestTemplate.class);
//         restTemplateBuilder = org.mockito.Mockito.mock(RestTemplateBuilder.class);
//         when(restTemplateBuilder.build()).thenReturn(restTemplate);
//         when(restTemplate.getForObject(anyString(), eq(int.class))).thenReturn(7);

//         mockMvc = MockMvcBuilders.standaloneSetup(new PlayersController(restTemplateBuilder)).build();
//     }

//     @Test
//     void getPlayersEndpointReturnsPlayerRankings() throws Exception {
//         mockMvc.perform(get("/players"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.length()" ).value(23))
//                 .andExpect(jsonPath("$[0].player").value("Aitana Bonmatí"))
//                 .andExpect(jsonPath("$[0].ranking").value(7));
//     }
// }

package com.packt.footballclient.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClient;

class PlayersControllerMvcTests {

    private MockMvc mockMvc;

    // Ersetzen der alten RestTemplate-Komponenten durch die RestClient-Gegenstücke
    private RestClient.Builder restClientBuilder;
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        // RETURNS_DEEP_STUBS erlaubt das automatische Mocken der verketteten Fluent-API-Aufrufe
        restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);
        restClientBuilder = mock(RestClient.Builder.class);

        // Konfiguration des Builders, wie er im Controller-Konstruktor verwendet wird
        when(restClientBuilder.baseUrl(anyString())).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);

        // Simuliert das Verhalten von: restClient.get().uri("/{player}", player).retrieve().body(Integer.class)
        when(restClient.get()
                .uri(anyString(), any(Object[].class))
                .retrieve()
                .body(eq(Integer.class)))
                .thenReturn(7);

        // Übergabe des gemockten Builders an den neuen Controller-Konstruktor
        mockMvc = MockMvcBuilders.standaloneSetup(new PlayersController(restClientBuilder)).build();
    }

    @Test
    void getPlayersEndpointReturnsPlayerRankings() throws Exception {
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(23))
                .andExpect(jsonPath("$[0].player").value("Aitana Bonmatí"))
                .andExpect(jsonPath("$[0].ranking").value(7));
    }
}
