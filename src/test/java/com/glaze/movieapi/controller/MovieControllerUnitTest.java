package com.glaze.movieapi.controller;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaze.movieapi.dto.in.CreateMovieRequest;
import com.glaze.movieapi.exceptions.NotFoundException;
import com.glaze.movieapi.service.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(MovieController.class)
@DisplayName("Movie Controller Unit Tests")
public class MovieControllerUnitTest {

    private final static String BASE_ENDPOINT = "/api/v1/movie";
    @MockBean private MovieService movieService;
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;

    @Test
    @DisplayName("Given a valid request respond with 200 status")
    void givenAValidRequestWhenSaveShouldRespondOk() throws Exception{
        // Given
        CreateMovieRequest movieRequest = new CreateMovieRequest(
            "Blade runner 2047",
            "Great sequel from the awesome 1982 film",
            "sci-fi",
            LocalDate.of(2017, 10, 3)
        );
        String json = mapper.writeValueAsString(movieRequest);

        // When
        when(movieService.save(movieRequest)).thenReturn(1L);

        // Then
        mvc.perform(
            post(BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            )
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Given an invalid request when saving should respond with bad request")
    void givenAnInvalidRequestWhenSavingShouldRespondBadRequest() throws Exception {
        // Then
        CreateMovieRequest request = new CreateMovieRequest("", "", "", null);
        String json = mapper.writeValueAsString(request);

        // Then
        mvc.perform(
            post(BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given a existing id when deleting should respond with no content")
    void givenAExistingIdWhenDeletingRespondWithNoContent() throws Exception {
        // Given
        Long existingId = 2047L;

        // When
        doNothing().when(movieService)
            .deleteById(existingId);

        // Then
        mvc.perform(
            delete(String.format("%s/%d", BASE_ENDPOINT, existingId))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Given a non existing id when deleting should respond with not found")
    void givenANonExistingIdWhenDeleteShouldRespondWithNotFound() throws Exception {
        // Given
        Long nonExistingId = 2047L;

        // When
        doThrow(new NotFoundException("movie.not-found", nonExistingId))
            .when(movieService)
            .deleteById(nonExistingId);

        // Then
        mvc.perform(
            delete(String.format("%s/%d", BASE_ENDPOINT, nonExistingId))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound());
    }

}
