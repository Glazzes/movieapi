package com.glaze.movieapi.dto.out;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record ActorResponse (
    Long id,
    String name,
    String summary,
    LocalDate birthDate,
    LocalDate lastModified
){}
