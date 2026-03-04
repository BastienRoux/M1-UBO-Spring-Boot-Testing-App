package com.controllers;

import com.dtos.MovieDto;
import com.services.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/films") // Utiliser /films pour être cohérent avec le frontend
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
    public ResponseEntity<MovieDto> getMovie(@PathVariable Integer id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping
    public ResponseEntity<MovieDto> saveMovie(@RequestBody MovieDto movieDto) {
        // Retourne un statut 201 Created pour la création
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.saveMovie(movieDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Integer id, @RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        // Retourne un 204 No Content en cas de succès, parfait pour une suppression
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieDto>> getMoviesByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(movieService.getMoviesByGenre(genre));
    }

    @PatchMapping("/{id}/rental")
    public ResponseEntity<MovieDto> toggleRental(
            @PathVariable Integer id,
            @RequestBody java.util.Map<String, Object> updates) {

        Boolean isOpen = updates.containsKey("isOpen") ? (Boolean) updates.get("isOpen") : null;
        Object priceObj = updates.get("price");
        Double price = null;
        if (priceObj != null) {
            price = Double.valueOf(priceObj.toString());
        }

        return ResponseEntity.ok(movieService.toggleRental(id, isOpen, price));
    }

    // ===== Gestion des artistes d'un film =====

    /**
     * Ajouter un artiste à un film
     */
    @PostMapping("/{movieId}/artists/{artistId}")
    public ResponseEntity<MovieDto> addArtistToMovie(
            @PathVariable Integer movieId,
            @PathVariable Long artistId) {
        return ResponseEntity.ok(movieService.addArtistToMovie(movieId, artistId));
    }

    /**
     * Retirer un artiste d'un film
     */
    @DeleteMapping("/{movieId}/artists/{artistId}")
    public ResponseEntity<MovieDto> removeArtistFromMovie(
            @PathVariable Integer movieId,
            @PathVariable Long artistId) {
        return ResponseEntity.ok(movieService.removeArtistFromMovie(movieId, artistId));
    }

    /**
     * Mettre à jour la liste complète des artistes d'un film
     */
    @PutMapping("/{movieId}/artists")
    public ResponseEntity<MovieDto> updateMovieArtists(
            @PathVariable Integer movieId,
            @RequestBody List<Long> artistIds) {
        return ResponseEntity.ok(movieService.updateMovieArtists(movieId, artistIds));
    }

    /**
     * Récupérer les films d'un artiste
     */
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<MovieDto>> getMoviesByArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(movieService.getMoviesByArtist(artistId));
    }
}