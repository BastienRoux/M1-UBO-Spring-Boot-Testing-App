package com.services.impl;

import com.dtos.MovieDto;
import com.repositories.MovieRepository;
import com.services.MovieService;
import com.mappers.MovieMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service("movieService")
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public MovieDto saveMovie(MovieDto movieDto) {
        var movie = movieMapper.toEntity(movieDto);
        var savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDto getMovieById(Integer movieId) {
        var movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Le film avec l'ID %d n'existe pas", movieId)));
        return movieMapper.toDto(movie);
    }

    @Override
    public boolean deleteMovie(Integer movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new EntityNotFoundException("Film introuvable");
        }
        movieRepository.deleteById(movieId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> getAllMovie() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toDto)
                .toList();
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto) {
        var existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Le film avec l'ID %d n'existe pas", movieId)));

        existingMovie.setTitle(movieDto.getTitle());
        existingMovie.setReleaseYear(movieDto.getReleaseYear());
        existingMovie.setDirector(movieDto.getDirector());

        var updatedMovie = movieRepository.save(existingMovie);
        return movieMapper.toDto(updatedMovie);
    }
}