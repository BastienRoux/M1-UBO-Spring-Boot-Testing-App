package com.controllers;

import com.dtos.MovieDto;
import com.services.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies") // Pluriel comme demandé dans l'OpenAPI
public class MovieController {

    private final MovieService movieService; // Toujours injecter l'interface et non l'implémentation !

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovies() {
        return ResponseEntity.ok(movieService.getAllMovie());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable Integer id){
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping
    public ResponseEntity<MovieDto> saveMovie(@RequestBody MovieDto movieDto){
        // Retourne un statut 201 Created pour la création
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.saveMovie(movieDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Integer id, @RequestBody MovieDto movieDto){
        return ResponseEntity.ok(movieService.updateMovie(id, movieDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer id){
        movieService.deleteMovie(id);
        // Retourne un 204 No Content en cas de succès, parfait pour une suppression
        return ResponseEntity.noContent().build();
    }
}