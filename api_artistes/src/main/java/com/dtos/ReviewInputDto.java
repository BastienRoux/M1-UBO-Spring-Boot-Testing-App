package com.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewInputDto {
	
	@NotNull(message = "La note est obligatoire")
	@Min(value = 1, message = "La note doit être au minimum 1")
	@Max(value = 5, message = "La note doit être au maximum 5")
	private Integer rating;
	
	@NotBlank(message = "Le commentaire est obligatoire")
	private String comment;
	
	// IDs pour les pseudo-jointures
	@NotNull(message = "L'ID du film est obligatoire")
	private Long movieId;
	
	@NotNull(message = "L'ID de l'utilisateur est obligatoire")
	private Long userId;
	
}
