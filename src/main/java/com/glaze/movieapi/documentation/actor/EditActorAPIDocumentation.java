package com.glaze.movieapi.documentation.actor;

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
@Operation(summary = "Edits an actor")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Actor edited successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "Actor does not exists by the given id",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProblemDetail.class))
    )
})
public @interface EditActorAPIDocumentation {}
