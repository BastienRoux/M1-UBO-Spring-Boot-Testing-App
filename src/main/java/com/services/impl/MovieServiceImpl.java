package com.services.impl;

import com.dtos.MovieDto;
import com.entities.Artist;
import com.entities.Movie;
import com.repositories.ArtistRepository;
import com.repositories.MovieRepository;
import com.services.MovieService;
import com.mappers.MovieMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service("movieService")
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ArtistRepository artistRepository;

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper,
            ArtistRepository artistRepository) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.artistRepository = artistRepository;
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
        existingMovie.setPrice(movieDto.getPrice());
        existingMovie.setMinAge(movieDto.getMinAge());
        existingMovie.setIsOpen(movieDto.getIsOpen());
        existingMovie.setGenres(movieDto.getGenres());

        var updatedMovie = movieRepository.save(existingMovie);
        return movieMapper.toDto(updatedMovie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> getMoviesByGenre(String genre) {
        return movieRepository.findByGenresContaining(genre).stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public MovieDto toggleRental(Integer movieId, Boolean isOpen, Double price) {
        var movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Le film avec l'ID %d n'existe pas", movieId)));

        if (isOpen != null) {
            movie.setIsOpen(isOpen);
        }
        if (price != null) {
            movie.setPrice(price);
        }

        var updatedMovie = movieRepository.save(movie);
        return movieMapper.toDto(updatedMovie);
    }

    @Override
    public MovieDto addArtistToMovie(Integer movieId, Long artistId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Film introuvable avec l'ID: " + movieId));

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artiste introuvable avec l'ID: " + artistId));

        movie.getArtists().add(artist);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    @Override
    public MovieDto removeArtistFromMovie(Integer movieId, Long artistId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Film introuvable avec l'ID: " + movieId));

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artiste introuvable avec l'ID: " + artistId));

        movie.getArtists().remove(artist);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    @Override
    public MovieDto updateMovieArtists(Integer movieId, List<Long> artistIds) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Film introuvable avec l'ID: " + movieId));

        // Récupérer tous les artistes par leurs IDs
        List<Artist> artists = artistRepository.findAllById(artistIds);

        // Remplacer la liste des artistes
        movie.setArtists(new HashSet<>(artists));
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> getMoviesByArtist(Long artistId) {
        // Vérifier que l'artiste existe
        if (!artistRepository.existsById(artistId)) {
            throw new EntityNotFoundException("Artiste introuvable avec l'ID: " + artistId);
        }

        // Récupérer tous les films et filtrer ceux qui contiennent cet artiste
        return movieRepository.findAll().stream()
                .filter(movie -> movie.getArtists().stream()
                        .anyMatch(artist -> artist.getId().equals(artistId)))
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }
}