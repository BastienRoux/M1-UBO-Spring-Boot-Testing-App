package com.services.impl;

import com.services.ReservationService;
import com.dtos.ReservationDto;
import com.mappers.ReservationMapper;
import com.repositories.ReservationRepository;
import com.repositories.MovieRepository;
import com.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("reservationService")
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    /**
     * Constructeur avec injection des dépendances
     * L'injection par constructeur est préférée à @Autowired car :
     * - Elle rend les dépendances obligatoires
     * - Elle facilite les tests unitaires
     * - Elle permet l'immutabilité
     */
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationMapper reservationMapper,
            MovieRepository movieRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     * Cette méthode est transactionnelle par défaut grâce à @Transactional sur la
     * classe
     */
    @Override
    public ReservationDto saveReservation(ReservationDto reservationDto) {
        var reservation = reservationMapper.toEntity(reservationDto);

        com.entities.Movie movieEntity = null;

        // Pseudo-jointure : lier le Movie via son ID si fourni
        if (reservationDto.getMovieId() != null) {
            movieEntity = movieRepository.findById(reservationDto.getMovieId().intValue())
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format("Le film avec l'ID %d n'existe pas", reservationDto.getMovieId())));

            if (movieEntity.getIsOpen() != null && !movieEntity.getIsOpen()) {
                throw new IllegalStateException("Ce film n'est actuellement pas disponible à la location.");
            }
            reservation.setMovie(movieEntity);
        }

        // Pseudo-jointure : lier l'User via son ID si fourni
        if (reservationDto.getUserId() != null) {
            var user = userRepository.findById(reservationDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format("L'utilisateur avec l'ID %d n'existe pas", reservationDto.getUserId())));

            if (movieEntity != null && movieEntity.getMinAge() != null) {
                if (user.getAge() == null || user.getAge() < movieEntity.getMinAge()) {
                    throw new IllegalStateException(String.format(
                            "Vous n'avez pas l'âge requis (%d ans) pour louer ce film.", movieEntity.getMinAge()));
                }
            }
            reservation.setUser(user);

            // Vérifier si une réservation active existe déjà pour ce film et cet
            // utilisateur
            if (reservationDto.getMovieId() != null) {
                var existingReservation = reservationRepository.findByMovieIdAndUserIdAndStatus(
                        reservationDto.getMovieId(), reservationDto.getUserId(), "ACTIVE");
                if (existingReservation.isPresent()) {
                    throw new IllegalStateException(
                            String.format("Vous avez déjà une réservation active pour ce film"));
                }
            }

            // Vérifier la limite de 3 réservations actives
            long activeCount = reservationRepository.countByUserIdAndStatus(reservationDto.getUserId(), "ACTIVE");
            if (activeCount >= 3) {
                throw new IllegalStateException(
                        "Vous avez atteint la limite de 3 réservations actives. Veuillez en annuler une avant d'en créer une nouvelle.");
            }
        }

        var savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(savedReservation);
    }

    /**
     * {@inheritDoc}
     * Utilisation de la méthode orElseThrow pour une gestion élégante des cas
     * d'erreur
     */
    @Override
    @Transactional(readOnly = true)
    public ReservationDto getReservationById(Long reservationId) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("La réservation avec l'ID %d n'existe pas", reservationId)));
        return reservationMapper.toDto(reservation);
    }

    /**
     * {@inheritDoc}
     * Annule la réservation en ajoutant la date de fin et en changeant le statut
     */
    @Override
    public boolean deleteReservation(Long reservationId) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("La réservation avec l'ID %d n'existe pas", reservationId)));

        reservation.setEndedAt(java.time.LocalDateTime.now());
        reservation.setStatus("ENDED");
        reservationRepository.save(reservation);
        return true;
    }

    /**
     * {@inheritDoc}
     * Utilisation de l'API Stream pour une transformation fonctionnelle des données
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * Recherche toutes les réservations d'un film (pseudo-jointure)
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByMovieId(Long movieId) {
        return reservationRepository.findByMovieId(movieId.intValue()).stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * Recherche toutes les réservations d'un utilisateur (pseudo-jointure)
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserRentedMovie(Long movieId, Long userId) {
        return reservationRepository.existsByMovieIdAndUserId(movieId, userId);
    }
}
