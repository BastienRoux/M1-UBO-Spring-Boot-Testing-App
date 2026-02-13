package com.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovieDto {

    @NotNull
    private Integer id;

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotBlank(message = "L'année est obligatoire")
    private Integer releaseYear;

    @NotBlank(message = "Le directeur est obligatoire")
    private String director;
}
