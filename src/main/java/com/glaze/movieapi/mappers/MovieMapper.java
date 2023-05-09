package com.glaze.movieapi.mappers;

import com.glaze.movieapi.dto.in.CreateMovieRequest;
import com.glaze.movieapi.dto.out.MovieResponse;
import com.glaze.movieapi.entities.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "genre", target = "genre")
    @Mapping(source = "releaseDate", target = "releaseDate")
    @Mapping(target = "votes", constant = "0L")
    @Mapping(target = "rating", constant = "0D")
    Movie mapCreateMovieRequestToMovie(CreateMovieRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "genre", target = "genre")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "releaseDate", target = "releaseDate", dateFormat = "dd MMM, yyyy")
    MovieResponse mapMovieToMovieResponse(Movie movie);

}
