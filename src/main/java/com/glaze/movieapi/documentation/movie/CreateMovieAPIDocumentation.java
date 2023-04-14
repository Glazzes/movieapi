package com.glaze.movieapi.documentation.movie;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ProblemDetail;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
    summary = "Creates a new movie",
    description = "There are not unique constraints when creating a movie"
)
@ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Created movie successfully",
        content = @Content(mediaType = "application/json")
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid input provided",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProblemDetail.class))
    )
})
public @interface CreateMovieAPIDocumentation {}
