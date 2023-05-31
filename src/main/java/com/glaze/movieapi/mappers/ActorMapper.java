package com.glaze.movieapi.mappers;

import com.glaze.movieapi.dto.in.CreateActorRequest;
import com.glaze.movieapi.dto.out.ActorResponse;
import com.glaze.movieapi.entities.Actor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movies", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "summary", target = "summary")
    @Mapping(source = "birthDate", target = "birthDate")
    Actor mapCreateActorRequestToActor(CreateActorRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "summary", target = "summary")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "lastModified", target = "lastModified")
    ActorResponse mapActorToActorResponse(Actor actor);
}
