package com.services;

import com.dtos.AuthResponse;
import com.dtos.LoginRequest;
import com.dtos.UserDto;
import com.dtos.UserInputDto;

import java.util.List;

public interface UserService {
    /**
     * Enregistrer un nouvel utilisateur
     */
    AuthResponse register(UserInputDto userInputDto);
    
    /**
     * Connexion d'un utilisateur
     */
    AuthResponse login(LoginRequest loginRequest);
    
    /**
     * Récupérer tous les utilisateurs
     */
    List<UserDto> getAllUsers();
    
    /**
     * Récupérer un utilisateur par son ID
     */
    UserDto getUserById(Long id);
    
    /**
     * Récupérer un utilisateur par son pseudo
     */
    UserDto getUserByPseudo(String pseudo);
}
