package com.glaze.movieapi.exceptions;

import java.util.Locale;
import jakarta.servlet.http.HttpServletRequest;

import com.glaze.movieapi.utils.ExceptionHandlerUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource source;
    public ApiExceptionHandler(MessageSource source) {
        this.source = source;
        super.setMessageSource(source);
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

}
