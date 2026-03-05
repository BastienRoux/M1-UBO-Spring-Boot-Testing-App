package com.repositories;

import com.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// On remplace Long par Integer ici
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
}