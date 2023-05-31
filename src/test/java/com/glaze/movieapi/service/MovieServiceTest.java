package com.glaze.movieapi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.glaze.movieapi.dto.in.CreateMovieRequest;
import com.glaze.movieapi.dto.out.MovieResponse;
import com.glaze.movieapi.entities.Movie;
import com.glaze.movieapi.exceptions.NotFoundException;
import com.glaze.movieapi.mappers.MovieMapper;
import com.glaze.movieapi.repository.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Movie Service Tests")
class MovieServiceTest {

    @InjectMocks private MovieService underTest;
    @Mock private MovieRepository movieRepository;
    @Mock private MovieMapper movieMapper;

    private CreateMovieRequest request;

    @BeforeEach
    void setUp() {
        request = new CreateMovieRequest(
            "Blade runner 2047",
            "Best sci-fi movie from 2017",
            LocalDate.now()
        );
    }

    @AfterEach
    void tearDown() {
        request = null;
    }

    @Test
    @DisplayName("Given a request when saving should return id")
    void testSave() {
        // Given
        long id = 1L;
        Movie movie = Movie.builder()
            .id(id)
            .title(request.title())
            .synopsis(request.synopsis())
            .releaseDate(request.releaseDate())
            .build();

        // When
        when(movieMapper.mapCreateMovieRequestToMovie(request))
            .thenReturn(movie);

        when(movieRepository.save(movie))
            .thenReturn(movie);

        Long responseId = underTest.save(request);

        // Then
        assertThat(responseId).isEqualTo(id);
        verify(movieMapper).mapCreateMovieRequestToMovie(request);
        verify(movieRepository).save(movie);
    }

    @Test
    @DisplayName("Given a request when find all return page")
    void testFindAll() {
        // Given
        PageRequest request = PageRequest.of(
            0,
            1,
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // When
        Movie movie = Movie.builder()
            .id(1L)
            .title("Blade runner 2047")
            .synopsis("Worthy sequel from the awesome 1982 film")
            .releaseDate(LocalDate.now())
            .rating(4.5D)
            .votes(1000L)
            .build();

        MovieResponse movieResponse = MovieResponse.builder()
            .id(movie.getId())
            .title(movie.getTitle())
            .synopsis(movie.getSynopsis())
            .rating(movie.getRating())
            .build();
        Page<Movie> moviePage = new PageImpl<>(List.of(movie), request, 1L);
        when(movieRepository.findAll(request))
            .thenReturn(moviePage);

        when(movieMapper.mapMovieToMovieResponse(movie))
            .thenReturn(movieResponse);

        Page<MovieResponse> response = underTest.findAll(request);

        // Then
        assertThat(response.isLast()).isTrue();
        assertThat(response.getTotalPages()).isEqualTo(1);
        verify(movieRepository).findAll(request);
        verify(movieMapper).mapMovieToMovieResponse(any(Movie.class));
    }

    @Test
    @DisplayName("Given an already saved movie should return a DTO")
    void testFindByIdSuccess() {
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
    void testFindByIdThrowException() {
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
    @DisplayName("Given a movie that exists when editing should succeed")
    void testEditByIdSuccess() {
        // Given
        long id = 1L;
        Movie movie = Movie.builder()
            .id(id)
            .title("Blade runner 2047")
            .synopsis("Worthy sequel from the awesome 1982 film")
            .releaseDate(LocalDate.now())
            .rating(4.5D)
            .votes(1000L)
            .build();

        // When
        Optional<Movie> movieOptional = Optional.of(movie);
        MovieResponse response = MovieResponse.builder()
            .id(id)
            .build();
        when(movieRepository.findById(id))
            .thenReturn(movieOptional);
        when(movieRepository.save(movie))
            .thenReturn(movie);
        when(movieMapper.mapMovieToMovieResponse(movie))
            .thenReturn(response);

        MovieResponse result = underTest.editById(id, request);

        // Then
        assertThat(result.id()).isEqualTo(id);
        spy(movie).setTitle(anyString());
        spy(movie).setSynopsis(anyString());
        spy(movie).setReleaseDate(any(LocalDate.class));
        verify(movieRepository).save(any(Movie.class));
        verify(movieMapper).mapMovieToMovieResponse(movie);
    }

    @Test
    @DisplayName("Given a non existing id when editing should throw exception")
    void testEditByIdThrowException() {
        // Given
        long id = 3000L;

        // When
        Optional<Movie> emptyOptional = Optional.empty();
        when(movieRepository.findById(id))
            .thenReturn(emptyOptional);

        // Then
        assertThatThrownBy(() -> underTest.editById(id, request))
            .isInstanceOf(NotFoundException.class);

        verify(movieRepository, never()).save(any(Movie.class));
        verify(movieMapper, never()).mapMovieToMovieResponse(any(Movie.class));
    }

    @Test
    @DisplayName("Given a saved movie should delete")
    void testDeleteByIdSuccess() {
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
    void testDeleteByIdThrowException() {
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
