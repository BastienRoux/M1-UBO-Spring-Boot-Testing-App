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
     * <p>
     * Get all Reservations in the system
     * </p>
     * 
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
    public ReservationDto getReservation(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    /**
     * Create a new Reservation in the system
     */
    @PostMapping
    public ReservationDto saveReservation(final @RequestBody ReservationDto reservationDto) {
        return reservationService.saveReservation(reservationDto);
    }

    /**
     * Cancel a Reservation by it's id (set endedAt and status to ENDED)
     */
    @DeleteMapping("/{id}")
    public Boolean deleteReservation(@PathVariable Long id) {
        return reservationService.deleteReservation(id);
    }

    /**
     * Obtenir toutes les réservations d'un film (pseudo-jointure)
     * 
     * @param movieId L'identifiant du film
     * @return Liste des réservations du film
     */
    @GetMapping("/movie/{movieId}")
    public List<ReservationDto> getReservationsByMovieId(@PathVariable Long movieId) {
        return reservationService.getReservationsByMovieId(movieId);
    }

    /**
     * Obtenir toutes les réservations d'un utilisateur (pseudo-jointure)
     * 
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des réservations de l'utilisateur
     */
    @GetMapping("/user/{userId}")
    public List<ReservationDto> getReservationsByUserId(@PathVariable Long userId) {
        return reservationService.getReservationsByUserId(userId);
    }

    /**
     * Endpoint interne permettant aux autres microservices de vérifier
     * si un utilisateur a le droit de poster un commentaire sur un film.
     */
    @GetMapping("/check")
    public boolean checkReservation(@RequestParam Long movieId, @RequestParam Long userId) {
        return reservationService.hasUserRentedMovie(movieId, userId);
    }
}
