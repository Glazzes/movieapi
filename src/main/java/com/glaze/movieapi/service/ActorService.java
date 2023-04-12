package com.glaze.movieapi.service;

import jakarta.transaction.Transactional;

import com.glaze.movieapi.dto.in.CreateActorRequest;
import com.glaze.movieapi.dto.out.ActorResponse;
import com.glaze.movieapi.entities.Actor;
import com.glaze.movieapi.exceptions.NotFoundException;
import com.glaze.movieapi.mappers.ActorMapper;
import com.glaze.movieapi.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;
    private final ActorMapper mapper;

    public Long save(CreateActorRequest request) {
        Actor actor = mapper.mapCreateActorRequestToActor(request);
        Actor savedActor = actorRepository.save(actor);
        return savedActor.getId();
    }

    public ActorResponse findById(Long id) {
        return actorRepository.findById(id)
            .map(mapper::mapActorToActorResponse)
            .orElseThrow(() -> new NotFoundException("", id));
    }

    public void deleteById(Long id) {
        boolean exists = actorRepository.existsById(id);
        if(!exists) {
            throw new NotFoundException("", id);
        }

        actorRepository.deleteById(id);
    }

}
