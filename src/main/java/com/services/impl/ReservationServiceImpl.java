package com.services.impl;

import com.services.ReservationService;
import com.dtos.ReservationDto;
import com.mappers.ReservationMapper;
import com.repositories.ReservationRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("reservationService")
@Transactional
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    /**
     * Constructeur avec injection des dépendances
     * L'injection par constructeur est préférée à @Autowired car :
     * - Elle rend les dépendances obligatoires
     * - Elle facilite les tests unitaires
     * - Elle permet l'immutabilité
     */
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    /**
     * {@inheritDoc}
     * Cette méthode est transactionnelle par défaut grâce à @Transactional sur la classe
     */
    @Override
    public ReservationDto saveReservation(ReservationDto reservationDto) {
        var reservation = reservationMapper.toEntity(reservationDto);
        var savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(savedReservation);
    }

    /**
     * {@inheritDoc}
     * Utilisation de la méthode orElseThrow pour une gestion élégante des cas d'erreur
     */
    @Override
    @Transactional(readOnly = true)
    public ReservationDto getReservationById(Long reservationId) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Le chien avec l'ID %d n'existe pas", reservationId)));
        return reservationMapper.toDto(reservation);
    }

    /**
     * {@inheritDoc}
     * La méthode deleteById ne lève pas d'exception si l'entité n'existe pas
     */
    @Override
    public boolean deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
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
}
