package com.glaze.movieapi.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.glaze.movieapi.documentation.movie.UpdateMovieAPIDocumentation;
import com.glaze.movieapi.documentation.movie.FindAllMoviesAPIDocumentation;
import com.glaze.movieapi.documentation.movie.DeleteMovieAPIDocumentation;
import com.glaze.movieapi.documentation.movie.FindMovieAPIDocumentation;
import com.glaze.movieapi.documentation.movie.CreateMovieAPIDocumentation;
import com.glaze.movieapi.dto.out.MovieResponse;
import com.glaze.movieapi.dto.in.CreateMovieRequest;
import com.glaze.movieapi.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/movie")
@RequiredArgsConstructor
@Tag(name = "Movie", description = "All actions applicable to movies only")
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    @CreateMovieAPIDocumentation
    public ResponseEntity<Long> save(@RequestBody @Valid CreateMovieRequest createMovieRequest) {
        Long movieId = movieService.save(createMovieRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(movieId);
    }

    @GetMapping
    @FindAllMoviesAPIDocumentation
    public ResponseEntity<Page<MovieResponse>> findAll(
        @RequestParam Integer size,
        @RequestParam Integer page,
        @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Sort sort = Sort.by(direction, "createdAt");
        PageRequest request = PageRequest.of(page, size, sort);
        Page<MovieResponse> response = movieService.findAll(request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @GetMapping("/{id}")
    @FindMovieAPIDocumentation
    public ResponseEntity<MovieResponse> findById(@PathVariable Long id) {
        MovieResponse movieResponse = movieService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
            .body(movieResponse);
    }

    @PatchMapping("/{id}")
    @UpdateMovieAPIDocumentation
    public ResponseEntity<MovieResponse> editById(
        @PathVariable Long id,
        @RequestBody @Valid CreateMovieRequest request
    ) {
        MovieResponse response = movieService.editById(id, request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @DeleteMapping("/{id}")
    @DeleteMovieAPIDocumentation
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        movieService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
