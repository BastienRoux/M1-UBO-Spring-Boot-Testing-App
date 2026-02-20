package com.mappers;

import com.dtos.MovieDto;
import com.entities.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieDto toDto(Movie movie) {
        if (movie == null) {
            return null;
        }

        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setReleaseYear(movie.getReleaseYear());
        movieDto.setDirector(movie.getDirector());
        return movieDto;
    }

    public Movie toEntity(MovieDto movieDto) {
        if (movieDto == null) {
            return null;
        }

        Movie movie = new Movie();
        if (movieDto.getId() != null) {
            movie.setId(movieDto.getId());
        }
        movie.setTitle(movieDto.getTitle());
        movie.setReleaseYear(movieDto.getReleaseYear());
        movie.setDirector(movieDto.getDirector());
        return movie;
    }
}