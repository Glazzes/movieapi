package com.glaze.movieapi.dto.out;

public record ReviewResponse(
    Long id,

    Long movieId,
    String content,

    Double rating
){}
