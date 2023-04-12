package com.glaze.movieapi.dto.in;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateActorRequest(
    @NotBlank
    String name,

    @NotBlank
    String summary,

    @NotNull
    LocalDate birthDate
) {}
