package com.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ReservationDto {

    @NotNull
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String name;
}
