package com.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReservationDto {

    @NotNull
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String name;
    
    private LocalDate date;
    
    // IDs pour les pseudo-jointures
    private Long movieId;
    private Long userId;
    
    private LocalDateTime createdAt;
    private LocalDateTime endedAt;
    private String status;
}
