package com.glaze.movieapi.dto.out;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Builder;

@Builder
public record MovieResponse(
    Long id,
    String title,
    String description,
    Double rating,
    String genre,
    LocalDate releaseDate
) implements Serializable {}
