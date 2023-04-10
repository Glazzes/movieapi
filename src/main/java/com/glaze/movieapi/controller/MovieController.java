package com.glaze.movieapi.controller;

import jakarta.validation.Valid;

import com.glaze.movieapi.dto.out.MovieResponse;
import com.glaze.movieapi.dto.in.CreateMovieRequest;
import com.glaze.movieapi.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody @Valid CreateMovieRequest createMovieRequest) {
        Long movieId = movieService.save(createMovieRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(movieId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> findById(@PathVariable Long id) {
        MovieResponse movieResponse = movieService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
            .body(movieResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        movieService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
