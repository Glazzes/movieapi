package com.glaze.movieapi.controller;

import jakarta.validation.Valid;

import com.glaze.movieapi.documentation.review.CreateReviewAPIDocumentation;
import com.glaze.movieapi.documentation.review.DeleteReviewAPIDocumentation;
import com.glaze.movieapi.documentation.review.FindAllReviewsAPIDocumentation;
import com.glaze.movieapi.documentation.review.UpdateReviewAPIDocumentation;
import com.glaze.movieapi.dto.out.ReviewResponse;
import com.glaze.movieapi.dto.in.CreateReviewRequest;
import com.glaze.movieapi.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "Applicable actions to reviews and movie associations")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/api/v1/movie/{movieId}/review")
    @CreateReviewAPIDocumentation
    public ResponseEntity<Long> saveMovieReview(
        @PathVariable Long movieId,
        @RequestBody @Valid CreateReviewRequest request
    ) {
        Long savedReviewId = reviewService.saveReview(movieId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(savedReviewId);
    }

    @GetMapping("/api/v1/movie/{movieId}/reviews")
    @FindAllReviewsAPIDocumentation
    public ResponseEntity<Page<ReviewResponse>> findAllMovieReviews(
        @PathVariable Long movieId,
        @RequestParam int page,
        @RequestParam int size
    ) {
        Pageable pageRequest = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "lastModified")
        );

        Page<ReviewResponse> content = reviewService.findAllMovieReviews(movieId, pageRequest);
        return ResponseEntity.status(HttpStatus.OK)
            .body(content);
    }

    @PatchMapping("/api/v1/review/{reviewId}")
    @UpdateReviewAPIDocumentation
    public ResponseEntity<ReviewResponse> updateReview(
        @PathVariable Long reviewId,
        @RequestBody @Valid CreateReviewRequest request
    ) {
        ReviewResponse response = reviewService.updateReview(reviewId, request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @DeleteMapping("/api/v1/review/{reviewId}")
    @DeleteReviewAPIDocumentation
    public ResponseEntity<Void> deleteMovieReview(@PathVariable Long reviewId) {
        reviewService.deleteMovieReview(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
