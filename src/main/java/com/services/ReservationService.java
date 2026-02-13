package com.services;

import com.dtos.DogDto;
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
     * Supprime une reservation du système
     * @param reservationId l'identifiant de la reservation à supprimer
     * @return true si la suppression a réussi
     */
    boolean deleteReservation(Long reservationId);

    /**
     * Récupère tous les reservation du système
     * @return la liste des reservation
     */
    List<ReservationDto> getAllReservation();
}
