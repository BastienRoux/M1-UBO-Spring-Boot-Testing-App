package com.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDto {
    
    private Long id;
    
    @NotNull(message = "L'ID du film est obligatoire")
    private Long movieId;
    
    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long userId;
    
    private LocalDateTime startedAt;
    
    private LocalDateTime endedAt;
    
    private String status;
    
    // Informations complémentaires pour l'affichage
    private String movieTitle;
    private String userPseudo;
}
