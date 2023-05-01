package com.glaze.movieapi.service;

import jakarta.transaction.Transactional;

import com.glaze.movieapi.dto.out.ReviewResponse;
import com.glaze.movieapi.entities.Review;
import com.glaze.movieapi.mappers.ReviewMapper;
import com.glaze.movieapi.dto.in.CreateReviewRequest;
import com.glaze.movieapi.entities.Movie;
import com.glaze.movieapi.exceptions.NotFoundException;
import com.glaze.movieapi.repository.MovieRepository;
import com.glaze.movieapi.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final ReviewMapper reviewMapper;
    private static final String MOVIE_NOT_FOUND = "movie.not-found";
    private static final String REVIEW_NOT_FOUND = "review.not-found";

    public Long saveReview(Long movieId, CreateReviewRequest request) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new NotFoundException(MOVIE_NOT_FOUND, movieId.toString()));

        Review review = reviewMapper.mapCreateReviewRequestToReviewEntity(request);
        review.setMovie(movie);

        Review savedReview = reviewRepository.save(review);
        return savedReview.getId();
    }

    public Page<ReviewResponse> findAllMovieReviews(Long movieId, Pageable page) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new NotFoundException(MOVIE_NOT_FOUND, movieId.toString()));

        return reviewRepository.findAllByMovie(movie, page)
            .map(reviewMapper::mapReviewEntityToReviewResponse);
    }

    public ReviewResponse updateReview(Long reviewId, CreateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new NotFoundException(REVIEW_NOT_FOUND, reviewId.toString()));

        review.setContent(request.content());
        review.setRating(request.rating());

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.mapReviewEntityToReviewResponse(updatedReview);
    }

    public void deleteMovieReview(Long reviewId) {
        boolean exists = reviewRepository.existsById(reviewId);
        if(!exists) {
            throw new NotFoundException(REVIEW_NOT_FOUND, reviewId.toString());
        }

        reviewRepository.deleteById(reviewId);
    }

}
