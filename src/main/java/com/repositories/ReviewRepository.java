package com.repositories;

import com.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	
	// Pseudo-jointure : recherche par movie_id
	List<Review> findByMovieId(Long movieId);
	
	// Pseudo-jointure : recherche par user_id
	List<Review> findByUserId(Long userId);
}
