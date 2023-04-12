package com.glaze.movieapi.controller;

import jakarta.validation.Valid;

import com.glaze.movieapi.dto.out.ActorResponse;
import com.glaze.movieapi.dto.in.CreateActorRequest;
import com.glaze.movieapi.service.ActorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/actor")
@RequiredArgsConstructor
@Tag(name = "Actor", description = "All actions that are only applicable to actors")
public class ActorController {

    private final ActorService actorService;

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody @Valid CreateActorRequest request) {
        Long id = actorService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorResponse> findById(@PathVariable Long id) {
        ActorResponse response = actorService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        actorService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
