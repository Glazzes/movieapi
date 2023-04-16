package com.glaze.movieapi.documentation.actor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.glaze.movieapi.dto.out.ActorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ProblemDetail;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(summary = "Find an actor by id")
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Actor found successfully",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ActorResponse.class))
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Actor does not exists with the given id",
        content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
})
public @interface FindActorAPIDocumentation {}
