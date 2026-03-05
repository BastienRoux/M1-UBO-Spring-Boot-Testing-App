package com.mappers;

import com.dtos.ReservationDto;
import com.entities.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationDto toDto(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setStartedAt(reservation.getStartedAt());
        reservationDto.setEndedAt(reservation.getEndedAt());
        reservationDto.setStatus(reservation.getStatus());

        // Mapper les IDs et informations complémentaires
        if (reservation.getMovie() != null) {
            reservationDto.setMovieId(reservation.getMovie().getId().longValue());
            reservationDto.setMovieTitle(reservation.getMovie().getTitle());
        }

        if (reservation.getUser() != null) {
            reservationDto.setUserId(reservation.getUser().getId());
            reservationDto.setUserPseudo(reservation.getUser().getPseudo());
        }

        return reservationDto;
    }

    public Reservation toEntity(ReservationDto reservationDto) {
        if (reservationDto == null) {
            return null;
        }

        Reservation reservation = new Reservation();
        if (reservationDto.getId() != null) {
            reservation.setId(reservationDto.getId());
        }
        reservation.setStartedAt(reservationDto.getStartedAt());
        reservation.setEndedAt(reservationDto.getEndedAt());
        reservation.setStatus(reservationDto.getStatus());

        // Note: Les relations Movie et User seront gérées dans le service
        // car on a besoin de charger les entités depuis la base

        return reservation;
    }
}
