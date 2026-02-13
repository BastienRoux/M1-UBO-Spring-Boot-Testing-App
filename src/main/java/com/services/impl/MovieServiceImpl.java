package com.services.impl;

import com.dtos.MovieDto;
import com.entities.Dog;
import com.repositories.MovieRepository;
import com.services.movieService;
import com.mappers.MovieMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implémentation des opérations métier pour la gestion des films.
 * Cette classe suit le principe de Single Responsibility (SOLID).
 */
@Service("movieService")
@Transactional
public class MovieServiceImpl implements movieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    /**
     * Constructeur avec injection des dépendances
     * L'injection par constructeur est préférée à @Autowired car :
     * - Elle rend les dépendances obligatoires
     * - Elle facilite les tests unitaires
     * - Elle permet l'immutabilité
     */
    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    /**
     * {@inheritDoc}
     * Cette méthode est transactionnelle par défaut grâce à @Transactional sur la classe
     */
    @Override
    public MovieDto saveMovie(MovieDto movieDto) {
        var movie = movieMapper.toEntity(movieDto);
        var savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    /**
     * {@inheritDoc}
     * Utilisation de la méthode orElseThrow pour une gestion élégante des cas d'erreur
     */
    @Override
    @Transactional(readOnly = true)
    public MovieDto getMovieById(Integer movieId) {
        var movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Le film avec l'ID %d n'existe pas", movieId)));
        return movieMapper.toDto(movie);
    }

    /**
     * {@inheritDoc}
     * La méthode deleteById ne lève pas d'exception si l'entité n'existe pas
     */
    @Override
    public boolean deleteMovie(Integer movieId) {
        movieRepository.deleteById(movieId);
        return true;
    }

    /**
     * {@inheritDoc}
     * Utilisation de l'API Stream pour une transformation fonctionnelle des données
     */
    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> getAllMovie() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toDto)
                .toList();
    }
}
