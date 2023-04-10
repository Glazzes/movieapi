package com.glaze.movieapi.controller;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaze.movieapi.dto.in.CreateMovieRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("Movie Controller Integration Tests")
public class MovieControllerIntegrationTest {
    private static final String BASE_ENDPOINT = "/api/v1/movie";
    @Autowired private ObjectMapper mapper;
    @Autowired private MockMvc mvc;

    @Test
    @DisplayName("Given a valid movie request should create a movie then return it's id")
    void createMovie() throws Exception {
        // Given
        CreateMovieRequest movieRequest = new CreateMovieRequest(
            "Blade runner 2047",
            "Great sequel from the awesome 1982 film",
            "sci-fi",
            LocalDate.of(2017, 10, 3)
        );
        String json = mapper.writeValueAsString(movieRequest);

        // Then
        RequestBuilder request = MockMvcRequestBuilders.post(BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Given an invalid movie request when saving should return bad request")
    void fainOnSaveMovie() throws Exception {
        // Given
        CreateMovieRequest movieRequest = new CreateMovieRequest("", "", "", null);
        String content = mapper.writeValueAsString(movieRequest);

        // Then
        RequestBuilder request = MockMvcRequestBuilders.post(BASE_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content);

        mvc.perform(request)
            .andExpect(status().isBadRequest());
    }

}
