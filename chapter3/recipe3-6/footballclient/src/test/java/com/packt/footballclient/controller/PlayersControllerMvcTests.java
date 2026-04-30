package com.packt.footballclient.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

class PlayersControllerMvcTests {

    private MockMvc mockMvc;

    private RestTemplateBuilder restTemplateBuilder;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = org.mockito.Mockito.mock(RestTemplate.class);
        restTemplateBuilder = org.mockito.Mockito.mock(RestTemplateBuilder.class);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        when(restTemplate.getForObject(anyString(), eq(int.class))).thenReturn(7);

        mockMvc = MockMvcBuilders.standaloneSetup(new PlayersController(restTemplateBuilder)).build();
    }

    @Test
    void getPlayersEndpointReturnsPlayerRankings() throws Exception {
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()" ).value(23))
                .andExpect(jsonPath("$[0].player").value("Aitana Bonmatí"))
                .andExpect(jsonPath("$[0].ranking").value(7));
    }
}
