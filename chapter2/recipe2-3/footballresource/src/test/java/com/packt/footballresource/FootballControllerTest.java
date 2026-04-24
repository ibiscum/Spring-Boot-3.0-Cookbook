package com.packt.footballresource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FootballController.class)
class FootballControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "SCOPE_football:read")
    void testGetTeams() throws Exception {
        mockMvc.perform(get("/football/teams"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Argentina")));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_football:admin")
    void testAddTeam() throws Exception {
        String teamName = "TestTeam";

        mockMvc.perform(post("/football/teams")
                        .with(csrf())
                        .contentType(TEXT_PLAIN)
                        .content(teamName))
                .andExpect(status().isOk())
                .andExpect(content().string(teamName + " added"));
    }
}
