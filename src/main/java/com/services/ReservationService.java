package com.services;

import com.dtos.ReservationDto;

import java.util.List;

public interface ReservationService {
    /**
     * Sauvegarde une reservation dans le système
     * @param reservationDto les données de la reservation à sauvegarder
     * @return la reservation sauvegardé avec son ID généré
     */
    ReservationDto saveReservation(ReservationDto reservationDto);

    /**
     * Récupère une reservation par son identifiant
     * @param reservationId l'identifiant de la reservation recherché
     * @return la reservation trouvé
     * @throws jakarta.persistence.EntityNotFoundException si la reservation n'existe pas
     */
    ReservationDto getReservationById(Long reservationId);

    /**
     * Annule une reservation (met à jour endedAt et status à ENDED)
     * @param reservationId l'identifiant de la reservation à annuler
     * @return true si l'annulation a réussi
     */
    boolean deleteReservation(Long reservationId);

    /**
     * Récupère tous les reservation du système
     * @return la liste des reservation
     */
    List<ReservationDto> getAllReservations();
	
	/**
	 * Récupère toutes les réservations d'un film (pseudo-jointure)
	 * @param movieId l'identifiant du film
	 * @return la liste des réservations du film
	 */
	List<ReservationDto> getReservationsByMovieId(Long movieId);
	
	/**
	 * Récupère toutes les réservations d'un utilisateur (pseudo-jointure)
	 * @param userId l'identifiant de l'utilisateur
	 * @return la liste des réservations de l'utilisateur
	 */
	List<ReservationDto> getReservationsByUserId(Long userId);
}
