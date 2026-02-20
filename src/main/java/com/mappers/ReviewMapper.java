package com.mappers;

import com.dtos.ReviewDto;
import com.dtos.ReviewInputDto;
import com.entities.Review;
import org.springframework.stereotype.Component;

/**
 * Mapper responsable de la conversion entre les entités Review et les DTOs.
 * Un mapper permet de séparer la couche de persistance de la couche de présentation.
 * 
 * Points clés du pattern Mapper :
 * - Conversion bidirectionnelle entre DTO et Entity
 * - Gestion des null-safety
 * - Pas de logique métier, uniquement de la transformation
 */
@Component
public class ReviewMapper {

	/**
	 * Convertit une entité Review en DTO ReviewDto
	 * Cette méthode est utilisée pour exposer les données aux clients de l'API
	 * 
	 * @param review l'entité à convertir
	 * @return le DTO correspondant ou null si l'entité est null
	 */
	public ReviewDto toDto(Review review) {
		if (review == null) {
			return null;
		}
		
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setId(review.getId());
		reviewDto.setRating(review.getRating());
		reviewDto.setComment(review.getComment());
		reviewDto.setCreatedAt(review.getCreatedAt());
		return reviewDto;
	}

	/**
	 * Convertit un DTO ReviewInputDto en entité Review
	 * Cette méthode est utilisée pour créer de nouvelles entités à partir des données reçues
	 * Note: L'ID et la date de création sont générés automatiquement par l'entité
	 * 
	 * @param reviewInputDto le DTO à convertir
	 * @return l'entité correspondante ou null si le DTO est null
	 */
	public Review toEntity(ReviewInputDto reviewInputDto) {
		if (reviewInputDto == null) {
			return null;
		}
		
		Review review = new Review();
		review.setRating(reviewInputDto.getRating());
		review.setComment(reviewInputDto.getComment());
		return review;
	}
}
