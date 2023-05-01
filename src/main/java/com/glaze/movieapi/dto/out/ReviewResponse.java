package com.glaze.movieapi.dto.out;

import java.time.LocalDate;

public record ReviewResponse(
    Long id,
    Long movieId,
    String content,
    Double rating,
    LocalDate reviewAt,
    LocalDate lastModified
){}
