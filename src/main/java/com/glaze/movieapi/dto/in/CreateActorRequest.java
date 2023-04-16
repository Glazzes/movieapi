package com.glaze.movieapi.dto.in;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateActorRequest(
    @NotBlank(message = "{actor.name.required}")
    String name,

    @Size(min = 20, max=1800, message = "{actor.summary.required}")
    String summary,

    @NotNull(message = "{actor.birth-date.required}")
    LocalDate birthDate
) {}
