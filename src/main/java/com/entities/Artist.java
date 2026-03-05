package com.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = { "movies" })
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    
    private String genre;
    
    @Column(length = 1000)
    private String biography;

    // Role metier (ACTOR, DIRECTOR, etc.) pour les filtres demandes.
    private String role;

    // Relation Many-to-Many avec Movie (bidirectionnelle)
    @ManyToMany(mappedBy = "artists")
    private Set<Movie> movies = new HashSet<>();
}
