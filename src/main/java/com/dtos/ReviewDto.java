package com.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewDto {
	
	@NotNull
	private Long id;
	
	@NotNull
	@Min(value = 1, message = "La note doit être au minimum 1")
	@Max(value = 5, message = "La note doit être au maximum 5")
	private Integer rating;
	
	@NotBlank(message = "Le commentaire est obligatoire")
	private String comment;
	
	private LocalDateTime createdAt;
	
}
