package com.glaze.movieapi.documentation.actor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Find all actors",
    description = """
    Find all actors as a pagination object. Request always succeeds but in case of no actors
    content property is empty
    """
)
@ApiResponse(responseCode = "200", description = "Find all actors successfully")
public @interface FindAllActorsAPIDocumentation {}
