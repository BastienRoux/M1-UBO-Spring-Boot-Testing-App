package com.services;

import com.dtos.MovieDto;
import java.util.List;

public interface MovieService {
    MovieDto saveMovie(MovieDto movieDto);
    MovieDto getMovieById(Integer movieId);
    boolean deleteMovie(Integer movieId);
    List<MovieDto> getAllMovie();
    MovieDto updateMovie(Integer movieId, MovieDto movieDto);
}