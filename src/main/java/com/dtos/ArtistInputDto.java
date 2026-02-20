package com.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtistInputDto {
	
	@NotBlank(message = "Le nom est obligatoire")
	private String name;
	
	private String genre;
	
	private String biography;
	
}
