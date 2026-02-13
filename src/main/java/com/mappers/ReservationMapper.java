package com.mappers;

import com.dtos.DogDto;
import com.dtos.ReservationDto;
import com.entities.Dog;
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
        ReservationDto.setId(reservation.getId());
        ReservationDto.setName(reservation.getName());
        return reservationDto;
    }
}
