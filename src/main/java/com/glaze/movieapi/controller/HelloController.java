package com.glaze.movieapi.controller;

import java.util.Locale;

import com.glaze.movieapi.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final MovieService movieService;

    @GetMapping("/")
    public String hello(@RequestHeader(name = "Accept-Language", defaultValue = "us") Locale language) {
        return "Hello world";
    }
}
