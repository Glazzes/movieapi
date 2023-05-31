package com.glaze.movieapi.controller;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaze.movieapi.dto.in.CreateMovieRequest;
import com.glaze.movieapi.entities.Movie;
import com.glaze.movieapi.repository.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Movie Controller Integration Tests")
public class MovieControllerTest {
    private static final String BASE_ENDPOINT = "/api/v1/movie";
    private Long savedMovieId;
    private CreateMovieRequest validRequest;
    private CreateMovieRequest invalidRequest;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mvc;
    @Autowired private MovieRepository movieRepository;

    @BeforeEach
    void setUpDatabase() {
        Movie movie = Movie.builder()
            .id(null)
            .title("Blade runner 2047")
            .synopsis("Great sequel of the original 1982 film")
            .releaseDate(LocalDate.of(2017, 10, 3))
            .votes(0L)
            .rating(0D)
            .build();

        Movie savedMovie = movieRepository.save(movie);
        savedMovieId = savedMovie.getId();
    }

    @BeforeEach
    void setUpRequests() {
        validRequest = new CreateMovieRequest(
            "Blade runner 2047",
            "Great sequel of the original 1982 film",
            LocalDate.of(2017, 10, 3)
        );

        invalidRequest = new CreateMovieRequest("", "", null);
    }

    @AfterEach
    void tearDown() {
        movieRepository.deleteAll();
        savedMovieId = null;
    }

    @Test
    @DisplayName("Given a valid movie request should create a movie then return it's id")
    void testSaveMovieSuccess() throws Exception {
        // Given
        String json = objectMapper.writeValueAsString(validRequest);

        // Then
        mvc.perform(
            post(BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").isNumber());
    }

    @Test
    @DisplayName("Given an invalid movie request when saving should return BAD_REQUEST")
    void testSaveMovieBadRequest() throws Exception {
        // Given
        String content = objectMapper.writeValueAsString(invalidRequest);

        // Then
        mvc.perform(
            post(BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                jsonPath("$.instance").value(BASE_ENDPOINT),
                jsonPath("$.errors.*", hasSize(3))
            );
    }

    @Test
    @DisplayName("Given a request with right parameters should respond with OK")
    void testFindAllById() throws Exception {
        mvc.perform(
            get(BASE_ENDPOINT)
                .param("size", "10")
                .param("page", "0")
            )
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.content", hasSize(1)),
                jsonPath("$.last", is(true))
            );
    }

    @Test
    @DisplayName("Given a request with missing parameters respond BAD_REQUEST")
    void testFindAllByIdBadRequest() throws Exception {
        mvc.perform(get(BASE_ENDPOINT))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given a user id and a valid request when finding should respond OK")
    void testEditByIdUserIdExistsAndValidRequest() throws Exception {
        String url = "%s/%d".formatted(BASE_ENDPOINT, savedMovieId);
        String json = objectMapper.writeValueAsString(validRequest);

        mvc.perform(
            patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Given a user id that doesn't exists and a valid request when finding should respond NOT_FOUND")
    void testEditByIdUserIdNotExistsAndValidRequest() throws Exception {
        String url = "%s/%s".formatted(BASE_ENDPOINT, "999999999");
        String json = objectMapper.writeValueAsString(validRequest);

        mvc.perform(
            patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                jsonPath("$.instance").value(url)
            );
    }

    @Test
    @DisplayName("Given an invalid request when editing should respond BAD_REQUEST")
    void testEditByIdInvalidRequest() throws Exception {
        String url = "%s/%d".formatted(BASE_ENDPOINT, savedMovieId);
        String json = objectMapper.writeValueAsString(invalidRequest);

        mvc.perform(
            patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                jsonPath("$.instance").value(url),
                jsonPath("$.errors.*", hasSize(3))
            );
    }

    @Test
    @DisplayName("Given an user id that exists when finding should respond OK")
    void testFindByIdOk() throws Exception {
        String url = "%s/%d".formatted(BASE_ENDPOINT, savedMovieId);

        mvc.perform(
            get(url)
            )
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.id").isNumber(),
                jsonPath("$.title").value(validRequest.title()),
                jsonPath("$.synopsis").value(validRequest.synopsis()),
                jsonPath("$.releaseDate").value(validRequest.releaseDate().toString()),
                jsonPath("$.rating").isNumber()
            );
    }

    @Test
    @DisplayName("Given a user id that doesn't exists when finding should respond NOT_FOUND")
    void testFindByIdNotFound() throws Exception {
        String url = "%s/%s".formatted(BASE_ENDPOINT, "99999");

        mvc.perform(get(url))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                jsonPath("$.instance").value(url)
            );
    }

    @Test
    @DisplayName("Given a delete request when user exists should respond NO_CONTENT")
    void testDeleteByIdNoContent() throws Exception {
        String url = "%s/%d".formatted(BASE_ENDPOINT, savedMovieId);

        mvc.perform(
            delete(url).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Given a delete request when user does not exists respond NOT_FOUND")
    void testDeleteByIdNotFound() throws Exception {
        String url = "%s/%s".formatted(BASE_ENDPOINT, "2047");

        mvc.perform(
            delete(url).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

}
