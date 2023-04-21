package com.glaze.movieapi.service;

import jakarta.transaction.Transactional;

import com.glaze.movieapi.entities.Review;
import com.glaze.movieapi.mappers.ReviewMapper;
import com.glaze.movieapi.dto.in.CreateReviewRequest;
import com.glaze.movieapi.entities.Movie;
import com.glaze.movieapi.exceptions.NotFoundException;
import com.glaze.movieapi.repository.MovieRepository;
import com.glaze.movieapi.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final ReviewMapper reviewMapper;

    public Long saveMovieReview(Long movieId, CreateReviewRequest request) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new NotFoundException("", movieId));

        Review review = reviewMapper.mapCreateReviewRequestToReviewEntity(request);
        review.setMovie(movie);

        Review savedReview = reviewRepository.save(review);
        return savedReview.getId();
    }

    public void deleteMovieReview(Long reviewId) {
        boolean exists = reviewRepository.existsById(reviewId);
        if(!exists) {
            throw new NotFoundException("", reviewId);
        }

        reviewRepository.deleteById(reviewId);
    }

}
