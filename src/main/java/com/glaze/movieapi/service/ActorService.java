package com.glaze.movieapi.service;

import jakarta.transaction.Transactional;

import com.glaze.movieapi.dto.in.CreateActorRequest;
import com.glaze.movieapi.dto.out.ActorResponse;
import com.glaze.movieapi.entities.Actor;
import com.glaze.movieapi.exceptions.NotFoundException;
import com.glaze.movieapi.mappers.ActorMapper;
import com.glaze.movieapi.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ActorService {

    private static final String NOT_FOUND_MESSAGE_KEY = "actor.not-found";

    private final ActorRepository actorRepository;
    private final ActorMapper mapper;

    public Long save(CreateActorRequest request) {
        Actor actor = mapper.mapCreateActorRequestToActor(request);
        Actor savedActor = actorRepository.save(actor);
        return savedActor.getId();
    }

    public Page<ActorResponse> findAll(PageRequest pageRequest) {
        return actorRepository.findAll(pageRequest)
            .map(mapper::mapActorToActorResponse);
    }

    public ActorResponse findById(Long id) {
        return actorRepository.findById(id)
            .map(mapper::mapActorToActorResponse)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE_KEY, id));
    }

    public ActorResponse editById(Long id, CreateActorRequest request) {
        Actor actor = actorRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE_KEY, id));

        actor.setName(request.name());
        actor.setSummary(request.summary());
        actor.setBirthDate(request.birthDate());

        Actor editedActor = actorRepository.save(actor);
        return mapper.mapActorToActorResponse(editedActor);
    }

    public void deleteById(Long id) {
        boolean exists = actorRepository.existsById(id);
        if(!exists) {
            throw new NotFoundException(NOT_FOUND_MESSAGE_KEY, id);
        }

        actorRepository.deleteById(id);
    }

}
