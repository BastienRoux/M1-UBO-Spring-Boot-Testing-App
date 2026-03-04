package com.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class MovieDto {

    private Integer id;

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotNull(message = "L'année est obligatoire")
    private Integer releaseYear;

    @NotBlank(message = "Le directeur est obligatoire")
    private String director;

    private Double price;
    private Integer minAge;
    private Boolean isOpen;
    private Set<String> genres;

    // Liste des artistes associés au film
    private List<ArtistDto> artists;

    // Liste des IDs des artistes (pour faciliter les mises à jour)
    private List<Long> artistIds;
}