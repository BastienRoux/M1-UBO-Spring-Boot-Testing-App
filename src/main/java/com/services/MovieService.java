package com.services;

import com.dtos.MovieDto;

import java.util.List;

/**
 * Interface définissant les opérations métier disponibles pour la gestion des films.
 * Cette interface suit le principe d'Interface Segregation (SOLID).
 */
public interface MovieService {
    /**
     * Sauvegarde un film dans le système
     * @param movieDto les données du film à sauvegarder
     * @return le film sauvegardé avec son ID généré
     */
    MovieDto saveMovie(MovieDto movieDto);

    /**
     * Récupère un film par son identifiant
     * @param movieId l'identifiant du film recherché
     * @return le film trouvé
     * @throws jakarta.persistence.EntityNotFoundException si le film n'existe pas
     */
    MovieDto getMovieById(Integer movieId);

    /**
     * Supprime un film du système
     * @param movieId l'identifiant du film à supprimer
     * @return true si la suppression a réussi
     */
    boolean deleteMovie(Integer movieId);

    /**
     * Récupère tous les films du système
     * @return la liste des films
     */
    List<MovieDto> getAllMovie();
}
