package com.glaze.movieapi.documentation.movie;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
    summary = "Finds movies by page",
    description = "Request always succeeds, when no items is just an empty page"
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Movie found successfully",
        content = @Content(mediaType = "application/json")
    )
})
public @interface FindAllMoviesAPIDocumentation {}
