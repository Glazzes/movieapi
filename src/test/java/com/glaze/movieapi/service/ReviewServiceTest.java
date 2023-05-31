package com.glaze.movieapi.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.glaze.movieapi.dto.in.CreateReviewRequest;
import com.glaze.movieapi.entities.Movie;
import com.glaze.movieapi.entities.Review;
import com.glaze.movieapi.exceptions.NotFoundException;
import com.glaze.movieapi.mappers.ReviewMapper;
import com.glaze.movieapi.repository.MovieRepository;
import com.glaze.movieapi.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Review Service Tests")
public class ReviewServiceTest {

    @InjectMocks private ReviewService reviewService;
    @Mock private ReviewRepository reviewRepository;
    @Mock private MovieRepository movieRepository;
    @Mock private ReviewMapper reviewMapper;
    private final CreateReviewRequest request = new CreateReviewRequest(
        "The movie wasn't so good as expected",
        3.5D
    );

    private final Movie movieEntity = Movie.builder()
        .id(1L)
        .title("Blade Runner 2047")
        .synopsis("Great sequel to the 1983 film")
        .votes(200L)
        .rating(4.5D)
        .createdAt(LocalDate.now())
        .releaseDate(LocalDate.now())
        .actors(Collections.emptySet())
        .reviews(Collections.emptySet())
        .build();

    private final Review reviewEntity = Review.builder()
        .id(1L)
        .content("Awesome movie")
        .rating(4.8D)
        .createdAt(LocalDate.now())
        .lastModified(LocalDate.now())
        .movie(movieEntity)
        .build();

    @Test
    @DisplayName("Save review successful")
    void saveReviewSuccess() {
        // Given
        Long movieId = 1L;

        // When
        Optional<Movie> movieOptional = Optional.of(movieEntity);
        when(movieRepository.findById(movieId)).thenReturn(movieOptional);
        when(reviewMapper.mapCreateReviewRequestToReviewEntity(request)).thenReturn(reviewEntity);
        when(reviewRepository.save(reviewEntity)).thenReturn(reviewEntity);
        Long savedId = reviewService.saveReview(movieId, request);

        // Then
        assertThat(savedId).isEqualTo(1L);
        verify(movieRepository).findById(any(Long.class));
        verify(reviewMapper).mapCreateReviewRequestToReviewEntity(request);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    @DisplayName("Save review throws exception")
    void saveReviewThrowsException() {
        // Given
        Long movieId = 1L;
        CreateReviewRequest request = new CreateReviewRequest("I loved the movie", 4.5D);

        // When
        Optional<Movie> emptyOptional = Optional.empty();
        when(movieRepository.findById(movieId)).thenReturn(emptyOptional);

        // Then
        assertThatThrownBy(() -> reviewService.saveReview(movieId, request))
            .isInstanceOf(NotFoundException.class);

        verify(reviewMapper, never()).mapCreateReviewRequestToReviewEntity(request);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    @DisplayName("Update review successful")
    void shouldUpdateReviewWhenExists() {
        // Given
        Long reviewId = 1L;

        // When
        Optional<Review> optional = Optional.of(reviewEntity);
        when(reviewRepository.findById(reviewId)).thenReturn(optional);
        when(reviewRepository.save(reviewEntity)).thenReturn(reviewEntity);
        reviewService.updateReview(reviewId, request);

        // Then
        verify(reviewMapper).mapReviewEntityToReviewResponse(any(Review.class));
    }

    @Test
    @DisplayName("UpdateReview throw exception")
    void shouldThrowExceptionWhenReviewDoesNoExists() {
        // Given
        Long reviewId = 1L;

        // When
        Optional<Review> optional = Optional.empty();
        when(reviewRepository.findById(reviewId)).thenReturn(optional);

        // Then
        assertThatThrownBy(() -> reviewService.updateReview(reviewId, request))
            .isInstanceOf(NotFoundException.class);

        verify(reviewRepository, never()).save(any(Review.class));
        verify(reviewMapper, never()).mapReviewEntityToReviewResponse(any(Review.class));
    }

    @Test
    @DisplayName("Find all movie reviews should succeed")
    void findAllMovieReviewsShouldSucceed() {
        // Given
        Long movieId = 1L;
        Pageable pageRequest = PageRequest.of(0, 1);

        // When
        Movie movie = new Movie(
            movieId,
            "Blade Runner 2047",
            "Great sequel to the 1983 film",
            100L,
            4.8D,
            LocalDate.now(),
            LocalDate.now(),
            null,
            null
        );
        Optional<Movie> movieOptional = Optional.of(movie);
        when(movieRepository.findById(movieId)).thenReturn(movieOptional);

        PageImpl<Review> reviews = new PageImpl<>(
            List.of(new Review(
                1L,
                "I loved it, it was great",
                4.5D,
                LocalDate.now(),
                LocalDate.now(),
                movie
            ))
        );
        when(reviewRepository.findAllByMovie(movie, pageRequest)).thenReturn(reviews);
        reviewService.findAllMovieReviews(movieId, pageRequest);

        // Then
        verify(reviewMapper).mapReviewEntityToReviewResponse(any(Review.class));
    }

    @Test
    @DisplayName("Find all movie reviews throw exception when movie does not exists")
    void findAllMovieReviewsShouldThrowException() {
        // Given
        Long nonExistingMovieId = 1L;
        Pageable pageRequest = PageRequest.of(0, 1);

        // When
        Optional<Movie> emptyOptional = Optional.empty();
        when(movieRepository.findById(nonExistingMovieId)).thenReturn(emptyOptional);

        // Then
        assertThatThrownBy(() -> reviewService.findAllMovieReviews(nonExistingMovieId, pageRequest))
            .isInstanceOf(NotFoundException.class);

        verify(reviewRepository, never()).findAllByMovie(any(Movie.class), any(Pageable.class));
        verify(reviewMapper, never()).mapReviewEntityToReviewResponse(any(Review.class));
    }

    @Test
    @DisplayName("Delete review success")
    void deleteReviewSuccess() {
        // Given
        Long reviewId = 1L;

        // When
        Optional<Review> reviewOptional = Optional.of(reviewEntity);
        when(reviewRepository.findById(reviewId)).thenReturn(reviewOptional);
        reviewService.deleteMovieReview(reviewId);

        // Then
        verify(movieRepository).save(movieEntity);
        verify(reviewRepository).deleteById(reviewId);
    }

    @Test
    @DisplayName("Delete review throw exception")
    void deleteReviewThrowException() {
        // Given
        Long reviewId = 1L;

        // When
        Optional<Review> emptyOptional = Optional.empty();
        when(reviewRepository.findById(reviewId)).thenReturn(emptyOptional);

        // Then
        assertThatThrownBy(() -> reviewService.deleteMovieReview(reviewId))
            .isInstanceOf(NotFoundException.class);

        verify(movieRepository, never()).save(any(Movie.class));
        verify(reviewRepository, never()).deleteById(reviewId);
    }

}
