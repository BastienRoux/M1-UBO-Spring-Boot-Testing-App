package com.mappers;

import com.dtos.MovieDto;
import com.dtos.MovieDto;
import com.entities.Movie;
import org.springframework.stereotype.Component;

/**
 * Mapper responsable de la conversion entre les entités Movie et les DTOs MovieDto.
 * Un mapper permet de séparer la couche de persistance de la couche de présentation.
 *
 * Points clés du pattern Mapper :
 * - Conversion bidirectionnelle entre DTO et Entity
 * - Gestion des null-safety
 * - Pas de logique métier, uniquement de la transformation
 */
@Component
public class MovieMapper {

    /**
     * Convertit une entité Movie en DTO MovieDto
     * Cette méthode est utilisée pour exposer les données aux clients de l'API
     *
     * @param movie l'entité à convertir
     * @return le DTO correspondant ou null si l'entité est null
     */
    public MovieDto toDto(Movie movie) {
        if (movie == null) {
            return null;
        }

        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setName(movie.getName());
        movieDto.setRace(movie.getRace());
        return movieDto;
    }

    /**
     * Convertit un DTO MovieDto en entité Movie
     * Cette méthode est utilisée pour persister les données reçues des clients
     * Note: La date de naissance n'est pas dans le DTO mais est présente dans l'entité
     *
     * @param movieDto le DTO à convertir
     * @return l'entité correspondante ou null si le DTO est null
     */
    public Movie toEntity(MovieDto movieDto) {
        if (movieDto == null) {
            return null;
        }

        Movie movie = new Movie();
        // On ne set l'ID que s'il existe (cas d'une mise à jour)
        if (movieDto.getId() != null) {
            movie.setId(movieDto.getId());
        }
        movie.setName(movieDto.getName());
        movie.setRace(movieDto.getRace());
        return movie;
    }
}