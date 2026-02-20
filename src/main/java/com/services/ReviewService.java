package com.services;

import com.dtos.ReviewDto;
import com.dtos.ReviewInputDto;

import java.util.List;

/**
 * Interface définissant les opérations métier disponibles pour la gestion des avis.
 * Cette interface suit le principe d'Interface Segregation (SOLID).
 */
public interface ReviewService {
	/**
	 * Crée un nouvel avis dans le système
	 * @param reviewInputDto les données de l'avis à créer
	 * @return l'avis créé avec son ID et sa date de création générés
	 */
	ReviewDto createReview(ReviewInputDto reviewInputDto);

	/**
	 * Récupère un avis par son identifiant
	 * @param reviewId l'identifiant de l'avis recherché
	 * @return l'avis trouvé
	 * @throws jakarta.persistence.EntityNotFoundException si l'avis n'existe pas
	 */
	ReviewDto getReviewById(Long reviewId);

	/**
	 * Met à jour un avis existant
	 * @param reviewId l'identifiant de l'avis à mettre à jour
	 * @param reviewInputDto les nouvelles données de l'avis
	 * @return l'avis mis à jour
	 * @throws jakarta.persistence.EntityNotFoundException si l'avis n'existe pas
	 */
	ReviewDto updateReview(Long reviewId, ReviewInputDto reviewInputDto);

	/**
	 * Supprime un avis du système
	 * @param reviewId l'identifiant de l'avis à supprimer
	 */
	void deleteReview(Long reviewId);

	/**
	 * Récupère tous les avis du système
	 * @return la liste des avis
	 */
	List<ReviewDto> getAllReviews();
}
