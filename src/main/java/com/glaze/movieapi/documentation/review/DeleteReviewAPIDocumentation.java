package com.glaze.movieapi.documentation.review;

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
@Operation(summary = "Deletes a review by id")
@ApiResponses({
    @ApiResponse(
        responseCode = "204",
        description = "Deleted review successfully",
        content = @Content(mediaType = "application/json")
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Review not found",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProblemDetail.class))
    )
})
public @interface DeleteReviewAPIDocumentation {}
