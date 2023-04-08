package com.glaze.movieapi.dto;

import java.io.Serializable;

public record MovieDTO(
    Long id,
    String title,
    String description,
    Double rating
) implements Serializable {}
