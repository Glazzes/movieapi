package com.glaze.movieapi.service;

import com.glaze.movieapi.dto.MovieDTO;
import com.glaze.movieapi.exceptions.NotFoundException;
import com.glaze.movieapi.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "movies")
@RequiredArgsConstructor
public class MovieService {

    private static final String MOVIE_NOT_FOUND = "movie.not-found";
    private final MovieRepository movieRepository;

    public String getAll() {
        return "All movies";
    }

    @Cacheable(key = "#id")
    public MovieDTO findById(Long id) {
        return movieRepository.findById(id)
            .map(movie -> new MovieDTO(1L, "Rambo", "Epic", 2.1D))
            .orElseThrow(() -> new NotFoundException(MOVIE_NOT_FOUND, id));
    }

    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        boolean exists = movieRepository.existsById(id);
        if(!exists) {
            throw new NotFoundException(MOVIE_NOT_FOUND, id);
        }

        movieRepository.deleteById(id);
    }

}
