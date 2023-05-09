package com.glaze.movieapi.documentation.review;

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
    summary = "Finds all reviews by a given movie id",
    description = "Request all succeeds when there is not content"
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Finds all reviews",
        content = @Content(mediaType = "application/json")
    )
})
public @interface FindAllReviewsAPIDocumentation {}
