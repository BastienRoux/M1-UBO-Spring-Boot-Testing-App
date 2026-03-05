package com.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserInputDto {
    
    @NotBlank(message = "Le pseudo est obligatoire")
    @Size(min = 3, max = 50, message = "Le pseudo doit contenir entre 3 et 50 caractères")
    private String pseudo;
    
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;
    
    @NotNull(message = "L'âge est obligatoire")
    @Min(value = 13, message = "Vous devez avoir au moins 13 ans")
    private Integer age;
    
    @NotBlank(message = "L'adresse est obligatoire")
    private String address;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;
}
