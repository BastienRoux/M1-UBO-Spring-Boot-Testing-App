package com.services;

import com.dtos.MovieDto;
import java.util.List;

public interface MovieService {
    MovieDto saveMovie(MovieDto movieDto);

    MovieDto getMovieById(Integer movieId);

    boolean deleteMovie(Integer movieId);

    List<MovieDto> getAllMovie();

    MovieDto updateMovie(Integer movieId, MovieDto movieDto);

    List<MovieDto> getMoviesByGenre(String genre);

    List<MovieDto> getMoviesByDirector(String director);

    MovieDto toggleRental(Integer movieId, Boolean isOpen, Double price);

    // Gestion des artistes
    MovieDto addArtistToMovie(Integer movieId, Long artistId);

    MovieDto removeArtistFromMovie(Integer movieId, Long artistId);

    MovieDto updateMovieArtists(Integer movieId, List<Long> artistIds);

    List<MovieDto> getMoviesByArtist(Long artistId);
}