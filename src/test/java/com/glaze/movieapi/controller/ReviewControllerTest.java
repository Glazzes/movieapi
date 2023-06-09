package com.glaze.movieapi.controller;

import java.time.LocalDate;
import java.util.Locale;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaze.movieapi.dto.in.CreateReviewRequest;
import com.glaze.movieapi.entities.Movie;
import com.glaze.movieapi.entities.Review;
import com.glaze.movieapi.repository.MovieRepository;
import com.glaze.movieapi.repository.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
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
public class ReviewControllerTest {

    private Long movieId;
    private Long reviewId;

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private MessageSource messageSource;
    private CreateReviewRequest validRequest;
    private CreateReviewRequest invalidRequest;

    @BeforeEach
    void setUpDatabaseRecords() {
        Movie movie = Movie.builder()
            .title("Blade runner 2047")
            .synopsis("Sequel of the great 1983 film")
            .releaseDate(LocalDate.now())
            .votes(0L)
            .rating(0D)
            .build();

        Movie savedMovie = movieRepository.save(movie);

        Review review = Review.builder()
            .content("I really liked this movie")
            .rating(4.5D)
            .movie(savedMovie)
            .build();

        Review savedReview = reviewRepository.save(review);
        this.movieId = savedMovie.getId();
        this.reviewId = savedReview.getId();
    }

    @BeforeEach
    void setUpObjects() {
        this.validRequest = new CreateReviewRequest(
            "This movie is nuts!!!",
            5D
        );

        this.invalidRequest = new CreateReviewRequest(
            "",
            100D
        );
    }

    @AfterEach
    void cleanTables() {
        reviewRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @Test
    @DisplayName("Save review successfully")
    void saveReview() throws Exception {
        String endpoint = "/api/v1/movie/%s/review".formatted(movieId);
        String content = objectMapper.writeValueAsString(validRequest);

        mvc.perform(
            post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
            )
            .andExpectAll(status().isCreated());
    }

    @Test
    @DisplayName("Save review with invalid input should respond with BAD_REQUEST")
    void saveReviewInvalidInput() throws Exception {
        String reviewId = this.movieId.toString();
        String endpoint = "/api/v1/movie/%s/review".formatted(reviewId);
        String content = objectMapper.writeValueAsString(invalidRequest);

        mvc.perform(
            post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.instance", is(endpoint)),
                jsonPath("$.status", is(400)),
                jsonPath("$.errors.*", hasSize(2))
            );
    }

    @Test
    @DisplayName("Find movie's reviews successfully")
    void findMovieReviewsSuccess() throws Exception {
        String endPoint = "/api/v1/movie/%s/reviews".formatted(this.movieId);

        mvc.perform(
            get(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
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
    @DisplayName("Find movie's reviews when no exists should respond with NOT_FOUND")
    void findMovieReviewsNotFound() throws Exception {
        String nonExistingMovieId = "2047";
        String endpoint = "/api/v1/movie/%s/reviews".formatted(nonExistingMovieId);

        String errorMessage = messageSource.getMessage(
            "movie.not-found",
            new Object[]{nonExistingMovieId},
            Locale.US
        );

        mvc.perform(
            get(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
            )
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.title", is(errorMessage)),
                jsonPath("$.instance", is(endpoint))
            );
    }

    @Test
    @DisplayName("Save review by id respond with NOT_FOUND")
    void saveReviewMovieNotFound() throws Exception {
        String nonExistingMovieId = "2047";
        String endpoint = "/api/v1/movie/%s/review".formatted(nonExistingMovieId);
        String content = objectMapper.writeValueAsString(validRequest);

        String errorMessage = messageSource.getMessage(
            "movie.not-found",
            new Object[]{nonExistingMovieId},
            Locale.US
        );
        mvc.perform(
            post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
            )
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.status", is(404)),
                jsonPath("$.title", is(errorMessage)),
                jsonPath("$.instance", is(endpoint))
            );
    }

    @Test
    @DisplayName("Update review successfully")
    void updateReviewSuccess() throws Exception {
        String endPoint = "/api/v1/review/%s".formatted(this.reviewId);
        String validContent = objectMapper.writeValueAsString(this.validRequest);

        mvc.perform(
            patch(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validContent)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.id").value(this.reviewId),
                jsonPath("$.movieId").value(this.movieId),
                jsonPath("$.content", is(this.validRequest.content())),
                jsonPath("$.rating", is(this.validRequest.rating()))
            );
    }

    @Test
    @DisplayName("Update review given invalid data should respond with BAD_REQUEST")
    void updateReviewWithInvalidData() throws Exception {
        String endPoint = "/api/v1/review/%s".formatted(this.reviewId);
        String content = objectMapper.writeValueAsString(this.invalidRequest);

        mvc.perform(
            patch(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
            )
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.instance", is(endPoint)),
                jsonPath("$.errors.*", hasSize(2))
            );
    }

    @Test
    @DisplayName("Update review when not exists should respond with NOT_FOUND")
    void updateReviewNotFound() throws Exception {
        String nonExistingReviewId = "2047";
        String endPoint = "/api/v1/review/%s".formatted(nonExistingReviewId);
        String content = objectMapper.writeValueAsString(this.validRequest);

        String errorMessage = messageSource.getMessage(
            "review.not-found",
            new Object[]{nonExistingReviewId},
            Locale.US
        );

        mvc.perform(
            patch(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
            )
            .andExpect(status().isNotFound())
            .andExpectAll(
                jsonPath("$.title", is(errorMessage)),
                jsonPath("$.instance", is(endPoint))
            );
    }

    @Test
    @DisplayName("Delete review by id should respond with NO_CONTENT")
    void deleteByIdSuccess() throws Exception {
        String endpoint = "/api/v1/review/%s".formatted(reviewId.toString());
        mvc.perform(delete(endpoint))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete review by id while does not exists should respond with NOT_FOUND")
    void deleteByIdNotFound() throws Exception {
        String nonExistingReviewId = "2047";
        String endpoint = "/api/v1/review/%s".formatted(nonExistingReviewId);

        String errorMessage = messageSource.getMessage(
            "review.not-found",
            new Object[]{nonExistingReviewId},
            Locale.US
        );

        mvc.perform(delete(endpoint))
            .andExpect(status().isNotFound())
            .andDo(print())
            .andExpectAll(
                jsonPath("$.title", is(errorMessage)),
                jsonPath("$.instance", is(endpoint)),
                jsonPath("$.status", is(404))
            );
    }

}
