package com.mappers;

import com.dtos.ReservationDto;
import com.entities.Reservation;

import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    /**
     * Convertit une entité Reservation en DTO ReservationDto
     * Cette méthode est utilisée pour exposer les données aux clients de l'API
     *
     * @param reservation l'entité à convertir
     * @return le DTO correspondant ou null si l'entité est null
     */
    public ReservationDto toDto(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setName(reservation.getName());
		reservationDto.setDate(reservation.getDate());
		reservationDto.setCreatedAt(reservation.getCreatedAt());
		reservationDto.setEndedAt(reservation.getEndedAt());
		reservationDto.setStatus(reservation.getStatus());
		
		// Mapper les IDs des pseudo-jointures
		if (reservation.getMovie() != null) {
		reservationDto.setMovieId(Long.valueOf(reservation.getMovie().getId()));
	}
	if (reservation.getUser() != null) {
		reservationDto.setUserId(reservation.getUser().getId());
	}
	
	return reservationDto;
}

/**
 * Convertit un DTO ReservationDto en entité Reservation
 * Cette méthode est utilisée pour persister les données reçues des clients
 * Note: La date de naissance n'est pas dans le DTO mais est présente dans l'entité
 * 
 * @param reservationDto le DTO à convertir
 * @return l'entité correspondante ou null si le DTO est null
 */
public Reservation toEntity(ReservationDto reservationDto) {
	if (reservationDto == null) {
		return null;
	}

	Reservation reservation = new Reservation();
	// On ne set l'ID que s'il existe (cas d'une mise à jour)
	if (reservationDto.getId() != null) {
		reservation.setId(reservationDto.getId());
	}
	reservation.setName(reservationDto.getName());
	reservation.setDate(reservationDto.getDate());
	reservation.setStatus(reservationDto.getStatus());
	// Les relations movie et user seront définies dans le service
	return reservation;
}
}
