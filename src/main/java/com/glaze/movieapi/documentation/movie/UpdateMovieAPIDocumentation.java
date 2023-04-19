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
@Operation(summary = "Finds a new movie by id")
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Movie updated successfully",
        content = @Content(mediaType = "application/json")
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Missing parameters or invalid request",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProblemDetail.class))
    )
})
public @interface UpdateMovieAPIDocumentation {}
