package com.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtistDto {
    
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String name;
    
    private String genre;
    
    private String biography;
}
