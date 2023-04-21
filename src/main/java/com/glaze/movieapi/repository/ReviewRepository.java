package com.glaze.movieapi.repository;

import com.glaze.movieapi.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {}
