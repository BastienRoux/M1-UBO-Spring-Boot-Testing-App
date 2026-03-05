package com.mappers;

import com.dtos.MovieDto;
import com.entities.Movie;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MovieMapper {

    private final ArtistMapper artistMapper;

    public MovieMapper(ArtistMapper artistMapper) {
        this.artistMapper = artistMapper;
    }

    public MovieDto toDto(Movie movie) {
        if (movie == null) {
            return null;
        }

        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setReleaseYear(movie.getReleaseYear());
        movieDto.setDirector(movie.getDirector());
        movieDto.setPrice(movie.getPrice());
        movieDto.setMinAge(movie.getMinAge());
        movieDto.setIsOpen(movie.getIsOpen());
        movieDto.setGenres(movie.getGenres());

        // Mapper les artistes vers DTOs
        if (movie.getArtists() != null && !movie.getArtists().isEmpty()) {
            movieDto.setArtists(movie.getArtists().stream()
                    .map(artistMapper::toDto)
                    .collect(Collectors.toList()));

            movieDto.setArtistIds(movie.getArtists().stream()
                    .map(artist -> artist.getId())
                    .collect(Collectors.toList()));
        }

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
        movie.setPrice(movieDto.getPrice());
        movie.setMinAge(movieDto.getMinAge());
        movie.setIsOpen(movieDto.getIsOpen());
        movie.setGenres(movieDto.getGenres());

        // Note: Les artistes seront gérés séparément dans le service
        // car on a besoin de charger les entités Artist depuis la base

        return movie;
    }
}