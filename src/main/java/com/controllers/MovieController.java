package com.controllers;

import com.dtos.DogDto;
import org.springframework.web.bind.annotation.*;

import com.services.impl.DogServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private final movieServiceImpl movieService;

    public MovieController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    /**
     * <p>Get all movie in the system</p>
     * @return List<MovieDto>
     */
    @GetMapping
    public List<MovieDto> getMovie() {
        return movieService.getAllMovie();
    }

    /**
     * Method to get the movie based on the ID
     */
    @GetMapping("/{id}")
    public MovieDto getMovie(@PathVariable Integer id){
        return movieService.getMovieById(id);
    }

    /**
     * Create a new Movie in the system
     */
    @PostMapping
    public MovieDto saveMovie(final @RequestBody MovieDto movieDto){
        return movieService.saveMovie(movieDto);
    }

    /**
     * Delete a movie by it's id
     */
    @DeleteMapping("/{id}")
    public Boolean deleteMovie(@PathVariable Integer id){
        return movieService.deleteMovie(id);
    }

}
