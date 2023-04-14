package com.glaze.movieapi.controller;

import jakarta.validation.Valid;

import com.glaze.movieapi.documentation.actor.CreateActorAPIDocumentation;
import com.glaze.movieapi.documentation.actor.DeleteActorAPIDocumentation;
import com.glaze.movieapi.documentation.actor.FindActorAPIDocumentation;
import com.glaze.movieapi.documentation.actor.FindAllActorsAPIDocumentation;
import com.glaze.movieapi.dto.out.ActorResponse;
import com.glaze.movieapi.dto.in.CreateActorRequest;
import com.glaze.movieapi.service.ActorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/actors")
@RequiredArgsConstructor
@Tag(name = "Actor", description = "All actions that are only applicable to actors")
public class ActorController {

    private final ActorService actorService;

    @GetMapping
    @FindAllActorsAPIDocumentation
    public ResponseEntity<Page<ActorResponse>> findAll(
        @RequestParam Integer page,
        @RequestParam Integer size,
        @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Sort sort = Sort.by(direction, "createdAt");
        PageRequest request = PageRequest.of(page, size, sort);
        Page<ActorResponse> response = actorService.findAll(request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @PostMapping
    @CreateActorAPIDocumentation
    public ResponseEntity<Long> save(@RequestBody @Valid CreateActorRequest request) {
        Long id = actorService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(id);
    }

    @GetMapping("/{id}")
    @FindActorAPIDocumentation
    public ResponseEntity<ActorResponse> findById(@PathVariable Long id) {
        ActorResponse response = actorService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @DeleteMapping("/{id}")
    @DeleteActorAPIDocumentation
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        actorService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
