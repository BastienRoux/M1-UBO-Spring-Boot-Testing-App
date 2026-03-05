package com.services.impl;

import com.dtos.ReviewDto;
import com.dtos.ReviewInputDto;
import com.entities.Review;
import com.repositories.ReviewRepository;
import com.repositories.MovieRepository;
import com.repositories.UserRepository;
import com.services.ReviewService;
import com.mappers.ReviewMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implémentation des opérations métier pour la gestion des avis.
 * Cette classe suit le principe de Single Responsibility (SOLID).
 */
@Service("reviewService")
@Transactional
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewMapper reviewMapper;
	private final MovieRepository movieRepository;
	private final UserRepository userRepository;

	/**
	 * Constructeur avec injection des dépendances
	 * L'injection par constructeur est préférée à @Autowired car :
	 * - Elle rend les dépendances obligatoires
	 * - Elle facilite les tests unitaires
	 * - Elle permet l'immutabilité
	 */
	public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper,
	                         MovieRepository movieRepository, UserRepository userRepository) {
		this.reviewRepository = reviewRepository;
		this.reviewMapper = reviewMapper;
		this.movieRepository = movieRepository;
		this.userRepository = userRepository;
	}

	/**
	 * {@inheritDoc}
	 * Cette méthode est transactionnelle par défaut grâce à @Transactional sur la classe
	 */
	@Override
	public ReviewDto createReview(ReviewInputDto reviewInputDto) {
		var review = reviewMapper.toEntity(reviewInputDto);
		
		// Pseudo-jointure : lier le Movie via son ID
		var movie = movieRepository.findById(reviewInputDto.getMovieId().intValue())
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Le film avec l'ID %d n'existe pas", reviewInputDto.getMovieId())));
		review.setMovie(movie);
		
		// Pseudo-jointure : lier l'User via son ID
		var user = userRepository.findById(reviewInputDto.getUserId())
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("L'utilisateur avec l'ID %d n'existe pas", reviewInputDto.getUserId())));
		review.setUser(user);
		
		var savedReview = reviewRepository.save(review);
		return reviewMapper.toDto(savedReview);
	}

	/**
	 * {@inheritDoc}
	 * Utilisation de la méthode orElseThrow pour une gestion élégante des cas d'erreur
	 */
	@Override
	@Transactional(readOnly = true)
	public ReviewDto getReviewById(Long reviewId) {
		var review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("L'avis avec l'ID %d n'existe pas", reviewId)));
		return reviewMapper.toDto(review);
	}

	/**
	 * {@inheritDoc}
	 * Met à jour un avis existant avec de nouvelles données
	 */
	@Override
	public ReviewDto updateReview(Long reviewId, ReviewInputDto reviewInputDto) {
		var existingReview = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("L'avis avec l'ID %d n'existe pas", reviewId)));
		
		// Mise à jour des champs
		existingReview.setRating(reviewInputDto.getRating());
		existingReview.setComment(reviewInputDto.getComment());
		
		// Mise à jour des pseudo-jointures si changées
		if (reviewInputDto.getMovieId() != null) {
			var movie = movieRepository.findById(reviewInputDto.getMovieId().intValue())
					.orElseThrow(() -> new EntityNotFoundException(
							String.format("Le film avec l'ID %d n'existe pas", reviewInputDto.getMovieId())));
			existingReview.setMovie(movie);
		}
		
		if (reviewInputDto.getUserId() != null) {
			var user = userRepository.findById(reviewInputDto.getUserId())
					.orElseThrow(() -> new EntityNotFoundException(
							String.format("L'utilisateur avec l'ID %d n'existe pas", reviewInputDto.getUserId())));
			existingReview.setUser(user);
		}
		
		var updatedReview = reviewRepository.save(existingReview);
		return reviewMapper.toDto(updatedReview);
	}

	/**
	 * {@inheritDoc}
	 * La méthode deleteById ne lève pas d'exception si l'entité n'existe pas
	 */
	@Override
	public void deleteReview(Long reviewId) {
		reviewRepository.deleteById(reviewId);
	}

	/**
	 * {@inheritDoc}
	 * Utilisation de l'API Stream pour une transformation fonctionnelle des données
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ReviewDto> getAllReviews() {
		return reviewRepository.findAll().stream()
				.map(reviewMapper::toDto)
				.toList();
	}
	
	/**
	 * {@inheritDoc}
	 * Recherche tous les avis d'un film (pseudo-jointure)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ReviewDto> getReviewsByMovieId(Long movieId) {
		return reviewRepository.findByMovieId(movieId).stream()
				.map(reviewMapper::toDto)
				.toList();
	}
	
	/**
	 * {@inheritDoc}
	 * Recherche tous les avis d'un utilisateur (pseudo-jointure)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ReviewDto> getReviewsByUserId(Long userId) {
		return reviewRepository.findByUserId(userId).stream()
				.map(reviewMapper::toDto)
				.toList();
	}
}
