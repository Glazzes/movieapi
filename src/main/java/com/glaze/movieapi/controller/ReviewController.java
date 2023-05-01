package com.glaze.movieapi.controller;

import jakarta.validation.Valid;

import com.glaze.movieapi.dto.out.ReviewResponse;
import com.glaze.movieapi.dto.in.CreateReviewRequest;
import com.glaze.movieapi.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "Applicable actions to reviews and movie associations")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/api/v1/movie/{movieId}/review")
    public ResponseEntity<Long> saveMovieReview(
        @PathVariable Long movieId,
        @RequestBody @Valid CreateReviewRequest request
    ) {
        Long savedReviewId = reviewService.saveMovieReview(movieId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(savedReviewId);
    }

    @PatchMapping("/api/v1/review/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
        @PathVariable Long reviewId,
        @RequestBody @Valid CreateReviewRequest request
    ) {
        ReviewResponse response = reviewService.updateReview(reviewId, request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @DeleteMapping("/api/v1/review/{reviewId}")
    public ResponseEntity<Void> deleteMovieReview(@PathVariable Long reviewId) {
        reviewService.deleteMovieReview(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
