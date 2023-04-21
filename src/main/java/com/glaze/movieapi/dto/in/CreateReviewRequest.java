package com.glaze.movieapi.dto.in;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record CreateReviewRequest(
    @Size(min = 10, max = 2000)
    String content,

    @Min(value = 0)
    @Max(value = 5)
    Double rating
) {}
