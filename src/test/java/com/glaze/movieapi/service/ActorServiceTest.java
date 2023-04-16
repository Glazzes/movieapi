package com.glaze.movieapi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.glaze.movieapi.dto.in.CreateActorRequest;
import com.glaze.movieapi.dto.out.ActorResponse;
import com.glaze.movieapi.entities.Actor;
import com.glaze.movieapi.exceptions.NotFoundException;
import com.glaze.movieapi.mappers.ActorMapper;
import com.glaze.movieapi.repository.ActorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Actor Service Tests")
class ActorServiceTest {

    @InjectMocks private ActorService underTest;
    @Mock private ActorRepository actorRepository;
    @Mock private ActorMapper actorMapper;

    @Test
    @DisplayName("Given a request returns appropriate page")
    void testFindAllActors() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 1);

        // When
        Actor actor = Actor.builder()
            .id(1L)
            .name("Adam Scott")
            .build();
        Page<Actor> page = new PageImpl<>(List.of(actor), pageRequest, 1);
        when(actorRepository.findAll(pageRequest))
            .thenReturn(page);

        Page<ActorResponse> response = underTest.findAll(pageRequest);

        // Then
        assertThat(response.isLast()).isTrue();
        assertThat(response.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("Given a request when save should succeed")
    void givenARequestWhenSaveShouldSucceed() {
        // Given
        CreateActorRequest request = new CreateActorRequest(
            "Ryan Gosling",
            "Ryan Thomas Gosling is a canadian actor and musician",
            LocalDate.of(1980, 11, 12)
        );
        Actor actor = Actor.builder()
                .id(1L)
                .name("Ryan Gosling")
                .summary("Ryan Thomas Gosling is a canadian actor and musician")
                .birthDate(LocalDate.of(1980, 11, 12))
                .build();

        // When
        when(actorMapper.mapCreateActorRequestToActor(request))
            .thenReturn(actor);
        when(actorRepository.save(actor))
            .thenReturn(actor);
        Long savedId = underTest.save(request);

        // Then
        assertThat(savedId).isEqualTo(1L);
        verify(actorMapper).mapCreateActorRequestToActor(request);
        verify(actorRepository).save(actor);
    }

    @Test
    @DisplayName("Given an existing id when find should return response")
    void givenAnExistingIdWhenFindShouldReturnResponse() {
        // Given
        Long id = 1L;

        // When
        Actor actor = Actor.builder()
            .id(id)
            .build();

        Optional<Actor> optional = Optional.ofNullable(actor);
        ActorResponse fakeResponse = ActorResponse.builder()
                .id(id)
                .build();
        when(actorRepository.findById(id)).thenReturn(optional);
        when(actorMapper.mapActorToActorResponse(actor)).thenReturn(fakeResponse);
        ActorResponse actualResponse = underTest.findById(id);

        // Then
        assertThat(actualResponse.id()).isEqualTo(id);
        verify(actorRepository).findById(id);
    }

    @Test
    @DisplayName("Given a non existing id when find should throw")
    void givenNonExistingIdWhenFindShouldThrow() {
        // Given
        Long id = 1L;

        // When
        Optional<Actor> emptyOptional = Optional.empty();
        when(actorRepository.findById(id)).thenReturn(emptyOptional);

        // Then
        assertThatThrownBy(() -> underTest.findById(id))
            .isInstanceOf(NotFoundException.class);
        verify(actorRepository).findById(id);
    }

    @Test
    @DisplayName("Given an existing id when delete should succeed")
    void givenAnExistingIdWhenDeleteShouldSucceed() {
        // Given
        Long id = 1L;

        // When
        when(actorRepository.existsById(id)).thenReturn(true);
        underTest.deleteById(id);

        // Then
        verify(actorRepository).deleteById(id);
    }

    @Test
    @DisplayName("Given a non existing id when delete should fail")
    void givenANonExistingIdWhenDeleteShouldFail() {
        // Given
        Long id = 1L;

        // When
        when(actorRepository.existsById(id)).thenReturn(false);

        // Then
        assertThatThrownBy(() -> underTest.deleteById(id))
            .isInstanceOf(NotFoundException.class);
        verify(actorRepository, never()).deleteById(id);
    }

}