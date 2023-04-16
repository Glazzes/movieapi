package com.glaze.movieapi.service;

import java.util.Optional;

import com.glaze.movieapi.dto.out.MovieResponse;
import com.glaze.movieapi.entities.Movie;
import com.glaze.movieapi.exceptions.NotFoundException;
import com.glaze.movieapi.mappers.MovieMapper;
import com.glaze.movieapi.repository.MovieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Movie Service Tests")
class MovieServiceTest {

    @InjectMocks private MovieService underTest;
    @Mock private MovieRepository movieRepository;
    @Mock private MovieMapper movieMapper;

    @Test
    @DisplayName("Given an already saved movie should return a DTO")
    void givenAMovieWhenExistsShouldReturnMovieDTO() {
        //Given
        long movieId = 1L;

        // When
        Movie movie = Movie.builder()
                .id(movieId)
                .build();

        MovieResponse movieResponse = MovieResponse.builder()
            .id(movieId)
            .build();

        Optional<Movie> optional = Optional.ofNullable(movie);
        when(movieRepository.findById(movieId)).thenReturn(optional);
        when(movieMapper.mapMovieToMovieResponse(movie)).thenReturn(movieResponse);
        MovieResponse result = underTest.findById(movieId);

        //Then
        assertThat(result).isEqualTo(movieResponse);
        verify(movieRepository).findById(movieId);
        verify(movieMapper).mapMovieToMovieResponse(movie);
    }

    @Test
    @DisplayName("Given a movie that does not exists must throw exception")
    void givenAMovieWhenNotExistsShouldReturnThrowException() {
        // Given
        long movieId = 1L;

        // When
        Optional<Movie> optional = Optional.empty();
        when(movieRepository.findById(movieId)).thenReturn(optional);

        // Then
        assertThatThrownBy(() -> underTest.findById(movieId))
            .isInstanceOf(NotFoundException.class);
        verify(movieRepository).findById(movieId);
    }

    @Test
    @DisplayName("Given a saved movie should delete")
    void givenAMovieWhenExistsShouldDelete() {
        // Given
        long movieId = 1L;

        // When
        when(movieRepository.existsById(movieId)).thenReturn(true);
        underTest.deleteById(movieId);

        // Then
        verify(movieRepository).deleteById(movieId);
    }

    @Test
    @DisplayName("Given a movie that does exists should throw exception")
    void givenAMovieWhenNotExistsShouldDeleteThrowException() {
        // Given
        long id = 1L;

        // When
        when(movieRepository.existsById(id)).thenReturn(false);

        // Then
        assertThatThrownBy(() -> underTest.deleteById(id))
            .isInstanceOf(NotFoundException.class);
        verify(movieRepository, never()).deleteById(id);
    }
}
