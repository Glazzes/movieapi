package com.glaze.movieapi.service;

import jakarta.transaction.Transactional;

import com.glaze.movieapi.dto.out.MovieResponse;
import com.glaze.movieapi.dto.in.CreateMovieRequest;
import com.glaze.movieapi.entities.Movie;
import com.glaze.movieapi.exceptions.NotFoundException;
import com.glaze.movieapi.mappers.MovieMapper;
import com.glaze.movieapi.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Transactional
@CacheConfig(cacheNames = "movies")
@RequiredArgsConstructor
public class MovieService {

    private static final String MOVIE_NOT_FOUND = "movie.not-found";
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public Long save(CreateMovieRequest request) {
        Movie movie = movieMapper.mapCreateMovieRequestToMovie(request);
        Movie savedMovie = movieRepository.save(movie);
        return savedMovie.getId();
    }

    @Cacheable(condition = "result.getSize() >= 10")
    public Page<MovieResponse> findAll(PageRequest request) {
        return movieRepository.findAll(request)
            .map(movieMapper::mapMovieToMovieResponse);
    }

    @Cacheable(key = "#id")
    public MovieResponse findById(Long id) {
        return movieRepository.findById(id)
            .map(movieMapper::mapMovieToMovieResponse)
            .orElseThrow(() -> new NotFoundException(MOVIE_NOT_FOUND, id));
    }

    @CachePut(key = "#id")
    public MovieResponse editById(Long id, CreateMovieRequest request) {
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(MOVIE_NOT_FOUND, id));

        movie.setTitle(request.title());
        movie.setDescription(request.description());
        movie.setGenre(request.genre());
        movie.setReleaseDate(request.releaseDate());

        Movie saved = movieRepository.save(movie);
        return movieMapper.mapMovieToMovieResponse(saved);
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
