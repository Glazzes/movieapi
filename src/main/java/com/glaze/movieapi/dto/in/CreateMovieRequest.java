package com.glaze.movieapi.dto.in;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateMovieRequest(
    @NotBlank(message = "{movie.name.blank}")
    @Size(message = "{movie.name.length}", min =3, max = 200)
    String title,

    @NotBlank(message = "{movie.description.blank}")
    @Size(message = "{movie.description.length}", min=20, max = 1800)
    String description,

    @NotBlank(message = "{movie.genre.required}")
    String genre,

    @NotNull(message = "{movie.release-date.required}")
    LocalDate releaseDate
) {}
