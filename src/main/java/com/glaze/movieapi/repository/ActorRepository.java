package com.glaze.movieapi.repository;

import com.glaze.movieapi.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {}
