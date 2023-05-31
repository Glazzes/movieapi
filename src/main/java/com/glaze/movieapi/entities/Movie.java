package com.glaze.movieapi.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "movies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "synopsis", nullable = false)
    private String synopsis;

    @Column(name = "votes", nullable = false)
    private Long votes;

    @Column(name = "rating")
    private Double rating;

    @CreatedDate
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "actors_movies",
        joinColumns = {@JoinColumn(name = "movie_id")},
        inverseJoinColumns = {@JoinColumn(name = "actor_id")}
    )
    private Set<Actor> actors = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

}
