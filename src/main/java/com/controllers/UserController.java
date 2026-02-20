package com.controllers;

import com.dtos.AuthResponse;
import com.dtos.LoginRequest;
import com.dtos.UserDto;
import com.dtos.UserInputDto;
import com.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserServiceImpl userService;
    
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }
    
    /**
     * Inscription d'un nouvel utilisateur
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserInputDto userInputDto) {
        AuthResponse response = userService.register(userInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Connexion d'un utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Récupérer tous les utilisateurs
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    /**
     * Récupérer un utilisateur par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    /**
     * Récupérer l'utilisateur connecté (simulation)
     * En production, récupérer depuis le token JWT
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        // Pour l'instant, retourne un utilisateur par défaut
        // En production, extraire l'ID du token JWT
        UserDto user = new UserDto();
        user.setId(1L);
        user.setPseudo("demo");
        user.setFirstName("Utilisateur");
        user.setLastName("Demo");
        user.setRole("USER");
        return ResponseEntity.ok(user);
    }
}
