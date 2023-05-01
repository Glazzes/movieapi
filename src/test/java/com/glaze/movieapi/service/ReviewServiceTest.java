package com.glaze.movieapi.service;

import java.util.Optional;

import com.glaze.movieapi.dto.in.CreateReviewRequest;
import com.glaze.movieapi.entities.Review;
import com.glaze.movieapi.exceptions.NotFoundException;
import com.glaze.movieapi.mappers.ReviewMapper;
import com.glaze.movieapi.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @Mock private ReviewMapper reviewMapper;
    private final CreateReviewRequest request = new CreateReviewRequest(
        "The movie wasn't so good as expected",
        3.5D
    );
    private final Review reviewEntity = new Review(1L, "Awesome movie", 4.8D, null);

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
        Optional<Review> optional = Optional.ofNullable(null);
        when(reviewRepository.findById(reviewId)).thenReturn(optional);

        // Then
        assertThatThrownBy(() -> reviewService.updateReview(reviewId, request))
            .isInstanceOf(NotFoundException.class);

        verify(reviewRepository, never()).save(any(Review.class));
        verify(reviewMapper, never()).mapReviewEntityToReviewResponse(any(Review.class));
    }

}
