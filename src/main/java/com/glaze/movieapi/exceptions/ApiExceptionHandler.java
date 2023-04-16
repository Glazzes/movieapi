package com.glaze.movieapi.exceptions;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;

import com.glaze.movieapi.utils.ExceptionHandlerUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final MessageSource source;
    public ApiExceptionHandler(MessageSource source) {
        this.source = source;
        // super.setMessageSource(source);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFoundException(
        NotFoundException e,
        HttpServletRequest request
    ) {
        Locale locale = ExceptionHandlerUtils.getLocaleFromRequest(request);
        String detail = source.getMessage(e.getMessageKey(), e.getArgs(), locale);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(detail);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(problem);
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException e) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        Map<String, List<String>> errors = e.getFieldErrors()
            .stream()
            .collect(Collectors.groupingBy(
                FieldError::getField,
                Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
            ));

        problem.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(problem);
    }

}
