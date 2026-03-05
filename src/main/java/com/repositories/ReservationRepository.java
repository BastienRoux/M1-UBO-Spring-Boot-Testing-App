package com.repositories;

import com.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	// Pseudo-jointure : recherche par movie_id
	List<Reservation> findByMovieId(Long movieId);

	// Pseudo-jointure : recherche par user_id
	List<Reservation> findByUserId(Long userId);

	// Trouver une réservation active pour un film et un utilisateur spécifiques
	Optional<Reservation> findByMovieIdAndUserIdAndStatus(Long movieId, Long userId, String status);

	// Compter les réservations actives d'un utilisateur
	long countByUserIdAndStatus(Long userId, String status);

	// Vérifier si un utilisateur a déjà réservé un film
	boolean existsByMovieIdAndUserId(Long movieId, Long userId);
}
