package com.packt.footballobs.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.packt.footballobs.service.AuctionService;
import com.packt.footballobs.service.DataService;
import com.packt.footballobs.service.FileLoader;
import com.packt.footballobs.service.TradingService;

import io.micrometer.observation.ObservationRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FootballController.class)
class FootballControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileLoader fileLoader;

    @MockBean
    private TradingService tradingService;

    @MockBean
    private ObservationRegistry observationRegistry;

    @MockBean
    private DataService dataService;

    @MockBean
    private AuctionService auctionService;

    @BeforeEach
    void setUp() {
        when(fileLoader.getTeams()).thenReturn(List.of("FC Barcelona", "Real Madrid"));
        when(tradingService.tradeCards(5)).thenReturn(5);
        when(dataService.getPlayerStats("Aitana Bonmatí")).thenReturn("some complex stats for player Aitana Bonmatí");
    }

    @Test
    void getTeamsReturnsLoadedTeamNames() throws Exception {
        mockMvc.perform(get("/football"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("FC Barcelona"))
                .andExpect(jsonPath("$[1]").value("Real Madrid"));
    }

    @Test
    void tradeCardsReturnsOrderCount() throws Exception {
        mockMvc.perform(post("/football").content("5").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void getPlayerStatsReturnsServiceResult() throws Exception {
        mockMvc.perform(get("/football/stats/Aitana Bonmatí"))
                .andExpect(status().isOk())
                .andExpect(content().string("some complex stats for player Aitana Bonmatí"));
    }

    @Test
    void addBidDelegatesToAuctionService() throws Exception {
        mockMvc.perform(post("/football/bid/Aitana Bonmatí").content("100").contentType("text/plain"))
                .andExpect(status().isOk());

        verify(auctionService).addBidAOP(eq("Aitana Bonmatí"), eq("100"));
    }
}
