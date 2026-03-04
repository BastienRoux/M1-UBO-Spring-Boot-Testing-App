package com.repositories;

import com.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// On remplace Long par Integer ici
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    // Recherche de films par genre
    List<Movie> findByGenresContaining(String genre);
}