package com.packt.footballpg;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.packt.footballpg.controller.PlayersController;
import com.packt.footballpg.entities.Player;
import com.packt.footballpg.service.PlayersService;

@WebMvcTest(PlayersController.class)
public class PlayersControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Ersetzt die echte Service-Bean im Spring-Kontext durch einen Mock
    private PlayersService playersService;

    @Test
    public void testGetPlayers_Success() throws Exception {
        Player player = new Player(); // Setzen Sie ggf. ID/Name über passende Methoden
        when(playersService.getPlayers()).thenReturn(Collections.singletonList(player));

        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetPlayer_Success() throws Exception {
        Player player = new Player();
        when(playersService.getPlayer(387138)).thenReturn(player);

        mockMvc.perform(get("/players/387138"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPlayer_InvalidIdType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/players/not-an-integer"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetPlayer_IdOverflow_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/players/2147483648")) // Integer.MAX_VALUE + 1
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreatePlayer_Success() throws Exception {
        Player mockPlayer = new Player();
        when(playersService.createPlayer(any(Player.class))).thenReturn(mockPlayer);

        // Ein valider JSON-String, der ein Player-Objekt repräsentiert
        String validPlayerJson = "{\"name\":\"Cristiano Ronaldo\",\"position\":\"Forward\"}";

        mockMvc.perform(post("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPlayerJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreatePlayer_MissingRequestBody_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")) // Komplett leerer Body
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreatePlayer_MalformedJson_ReturnsBadRequest() throws Exception {
        String malformedJson = "{\"name\":\"Messi\""; // Ungültiges JSON

        mockMvc.perform(post("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreatePlayer_UnsupportedMediaType_ReturnsUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/players")
                .contentType(MediaType.TEXT_PLAIN)
                .content("Nur einfacher Text"))
                .andExpect(status().isUnsupportedMediaType()); // 415 Error
    }
}
