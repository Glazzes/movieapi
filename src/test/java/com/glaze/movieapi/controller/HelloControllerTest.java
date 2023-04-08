package com.glaze.movieapi.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Hello controller integration tests")
public class HelloControllerTest {

    @Autowired private MockMvc mvc;

    @Test
    @DisplayName("Given a simple request returns 200 status code")
    public void testHelloEndpoint() throws Exception {
        // Given
        RequestBuilder request = MockMvcRequestBuilders.get("/");

        // When
        // Then
        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().string("All movies"));
    }

}
