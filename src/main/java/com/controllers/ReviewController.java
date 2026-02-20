package com.controllers;

import com.dtos.ReviewDto;
import com.dtos.ReviewInputDto;
import com.services.impl.ReviewServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
	
	private final ReviewServiceImpl reviewService;

	public ReviewController(ReviewServiceImpl reviewService) {
		this.reviewService = reviewService;
	}

	/**
	 * <p>Lister tous les avis</p>
	 * @return Liste des avis
	 */
	@GetMapping
	public ResponseEntity<List<ReviewDto>> getAllReviews() {
		return ResponseEntity.ok(reviewService.getAllReviews());
	}

	/**
	 * Obtenir un avis par ID
	 * @param id L'identifiant unique de l'avis
	 * @return L'avis trouvé
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ReviewDto> getReview(@PathVariable Long id) {
		return ResponseEntity.ok(reviewService.getReviewById(id));
	}

	/**
	 * Créer un nouvel avis
	 * @param reviewInputDto Les données de l'avis à créer
	 * @return L'avis créé avec son ID
	 */
	@PostMapping
	public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewInputDto reviewInputDto) {
		ReviewDto createdReview = reviewService.createReview(reviewInputDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
	}

	/**
	 * Mettre à jour un avis existant
	 * @param id L'identifiant de l'avis à mettre à jour
	 * @param reviewInputDto Les nouvelles données de l'avis
	 * @return L'avis mis à jour
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, 
	                                                @Valid @RequestBody ReviewInputDto reviewInputDto) {
		return ResponseEntity.ok(reviewService.updateReview(id, reviewInputDto));
	}

	/**
	 * Supprimer un avis
	 * @param id L'identifiant de l'avis à supprimer
	 * @return Réponse vide avec statut 204
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
		reviewService.deleteReview(id);
		return ResponseEntity.noContent().build();
	}
	
}
