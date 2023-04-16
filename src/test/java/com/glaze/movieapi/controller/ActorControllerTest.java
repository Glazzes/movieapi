package com.glaze.movieapi.controller;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaze.movieapi.dto.in.CreateActorRequest;
import com.glaze.movieapi.entities.Actor;
import com.glaze.movieapi.repository.ActorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Actor Controller Tests")
public class ActorControllerTest {

    private static final String BASE_ENDPOINT = "/api/v1/actors";

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ActorRepository actorRepository;

    private Long savedActorId;
    private CreateActorRequest validRequest;
    private CreateActorRequest invalidRequest;

    @BeforeEach
    void setUp() {
        Actor actor = Actor.builder()
            .id(null)
            .name("Adam Scott")
            .summary("He appeared on Severance!")
            .birthDate(LocalDate.now())
            .lastModified(null)
            .build();

        Actor saved = actorRepository.save(actor);
        /*
        Assigning the id it's necessary because of the sequence at database level.
        Hibernate will use nextval(sequence) rather than the given id
        */
        savedActorId = saved.getId();
    }

    @BeforeEach
    void setUpRequests() {
        validRequest =  new CreateActorRequest(
            "Ryan Gosling",
            "Canadian actor and musician",
            LocalDate.of(1980, 11, 12)
        );

        invalidRequest = new CreateActorRequest("", "", null);
    }

    @AfterEach
    void tearDown() {
       actorRepository.deleteAll();
       savedActorId = null;
    }

    @Test
    @DisplayName("Given a valid request when saving should respond with CREATED")
    void testSaveEndpoint() throws Exception {
        // Given
        String content = objectMapper.writeValueAsString(validRequest);

        // Then
        mvc.perform(
            post(BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").isNumber());
    }

    @Test
    @DisplayName("Given an invalid request when saving should respond with BAD_REQUEST")
    void testSaveBadRequest() throws Exception {
        String json = objectMapper.writeValueAsString(invalidRequest);

        mvc.perform(
            post(BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())),
                jsonPath("$.instance", is(BASE_ENDPOINT)),
                jsonPath("$.errors.*", hasSize(3))
            );
    }

    @Test
    @DisplayName("Given a request should return pageable object")
    void testFindAllReturnPage() throws Exception {
        mvc.perform(
                get(BASE_ENDPOINT)
                    .param("size", "10")
                    .param("page", "0")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.content", hasSize(1)),
                jsonPath("$.totalPages", is(1)),
                jsonPath("$.totalElements", is(1)),
                jsonPath("$.last", is(true))
            );
    }

    @Test
    @DisplayName("Given an existing user id when finding should respond with OK")
    void testFindByIdSuccess() throws Exception {
        String url = "%s/%d".formatted(BASE_ENDPOINT, 1);
        mvc.perform(get(url))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.id", is(1)),
                jsonPath("$.name", is("Adam Scott"))
            );
    }

    @Test
    @DisplayName("Given a non existing user id when finding should respond with NOT_FOUND")
    void testFindByIdNotFound() throws Exception {
        String url = "%s/%d".formatted(BASE_ENDPOINT, 2047);
        mvc.perform(get(url))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.status", is(404)),
                jsonPath("$.instance", is(url))
            );
    }

    @Test
    @DisplayName("Given a valid edit request when editing should respond with OK")
    void testEditByIdSuccess() throws Exception {
        String url = "%s/%d".formatted(BASE_ENDPOINT, savedActorId);
        String json = objectMapper.writeValueAsString(validRequest);

        mvc.perform(
            patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            )
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.name").value(validRequest.name()),
                jsonPath("$.summary").value(validRequest.summary()),
                jsonPath("$.birthDate").value(validRequest.birthDate().toString())
            );
    }

    @Test
    @DisplayName("Given an invalid request when editing should respond with BAD_REQUEST")
    void testEditByBadRequest() throws Exception {
        String url = "%s/%d".formatted(BASE_ENDPOINT, savedActorId);
        String json = objectMapper.writeValueAsString(invalidRequest);

        mvc.perform(
            patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            )
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.status", is(400)),
                jsonPath("$.instance", is(url)),
                jsonPath("$.errors.*", hasSize(3))
            );
    }

    @Test
    @DisplayName("Given a non existing user id when editing should respond with NOT_FOUND")
    void testEditByNotFound() throws Exception {
        long nonExistingUserId = 2047L;
        String url = "%s/%d".formatted(BASE_ENDPOINT, nonExistingUserId);
        String json = objectMapper.writeValueAsString(validRequest);

        mvc.perform(
                patch(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())),
                jsonPath("$.instance", is(url))
            );
    }

    @Test
    @DisplayName("Given an existing user id should delete and respond with NO_CONTENT")
    void testDeleteByIdSuccess() throws Exception {
        String url = "%s/%d".formatted(BASE_ENDPOINT, savedActorId);
        mvc.perform(delete(url))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Given a non existing user when delete should respond with NOT_FOUND")
    void testDeleteByIdFailure() throws Exception {
        String url = "%s/%s".formatted(BASE_ENDPOINT, "2047");
        mvc.perform(delete(url))
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())),
                jsonPath("$.instance", is(url))
            );
    }
}
