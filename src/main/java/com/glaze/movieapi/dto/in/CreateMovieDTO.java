package com.glaze.movieapi.dto.in;

import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

public record CreateMovieDTO(
    @NotBlank(message = "{movie.name.blank}")
    @Length(message = "{movie.name.length}", max = 200)
    String name,

    @NotBlank(message = "{}")
    @Length(message = "{}", max = 1800)
    String description
) {}
