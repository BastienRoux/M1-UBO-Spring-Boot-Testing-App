package com.controllers;

import com.dtos.ReservationDto;
import com.services.impl.ReservationServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationServiceImpl reservationService;

    public ReservationController(ReservationServiceImpl reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * <p>Get all Reservations in the system</p>
     * @return List<ReservationDto>
     */
    @GetMapping
    public List<ReservationDto> getReservations() {
        return reservationService.getAllReservations();
    }

    /**
     * Method to get the Reservation based on the ID
     */
    @GetMapping("/{id}")
    public ReservationDto getReservation(@PathVariable Long id){
        return reservationService.getReservationById(id);
    }

    /**
     * Create a new Reservation in the system
     */
    @PostMapping
    public ReservationDto saveReservation(final @RequestBody ReservationDto reservationDto){
        return reservationService.saveReservation(reservationDto);
    }

    /**
     * Delete a Reservation by it's id
     */
    @DeleteMapping("/{id}")
    public Boolean deleteReservation(@PathVariable Long id){
        return reservationService.deleteReservation(id);
    }


}
