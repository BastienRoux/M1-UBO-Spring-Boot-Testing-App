package com.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArtistDto {
	
	@NotNull
	private Long id;
	
	@NotBlank(message = "Le nom est obligatoire")
	private String name;
	
	private String genre;
	
	private String biography;
	
}
