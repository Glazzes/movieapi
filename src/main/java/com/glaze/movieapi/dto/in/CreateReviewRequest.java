package com.glaze.movieapi.dto.in;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record CreateReviewRequest(
    @Size(min = 10, max = 1200, message = "{review.content.length}")
    String content,

    @Min(value = 0, message = "{review.rating.min}")
    @Max(value = 5, message = "{review.rating.max}")
    Double rating
) {}
