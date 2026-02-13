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

    /**
     * Convertit un DTO ReservationDto en entité Reservation
     * Cette méthode est utilisée pour persister les données reçues des clients
     * Note: La date de naissance n'est pas dans le DTO mais est présente dans l'entité
     *
     * @param reservationDto le DTO à convertir
     * @return l'entité correspondante ou null si le DTO est null
     */
    public Dog toEntity(ReservationDto reservationDto) {
        if (reservationDto == null) {
            return null;
        }

        Dog dog = new Dog();
        // On ne set l'ID que s'il existe (cas d'une mise à jour)
        if (reservationDto.getId() != null) {
            dog.setId(reservationDto.getId());
        }
        dog.setName(reservationDto.getName());
        return dog;
    }

}
