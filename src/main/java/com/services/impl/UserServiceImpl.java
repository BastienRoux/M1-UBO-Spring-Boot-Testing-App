package com.services.impl;

import com.dtos.AuthResponse;
import com.dtos.LoginRequest;
import com.dtos.UserDto;
import com.dtos.UserInputDto;
import com.entities.User;
import com.mappers.UserMapper;
import com.repositories.UserRepository;
import com.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    
    @Override
    public AuthResponse register(UserInputDto userInputDto) {
        // Vérifier si le pseudo existe déjà
        if (userRepository.existsByPseudo(userInputDto.getPseudo())) {
            throw new IllegalArgumentException("Ce pseudo est déjà utilisé");
        }
        
        // Créer l'utilisateur
        User user = userMapper.toEntity(userInputDto);
        User savedUser = userRepository.save(user);
        
        // Générer un token simple (en production, utiliser JWT)
        String token = UUID.randomUUID().toString();
        
        return new AuthResponse(userMapper.toDto(savedUser), token);
    }
    
    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByPseudo(loginRequest.getPseudo())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        
        // Vérifier le mot de passe (en production, utiliser BCrypt)
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }
        
        // Générer un token simple (en production, utiliser JWT)
        String token = UUID.randomUUID().toString();
        
        return new AuthResponse(userMapper.toDto(user), token);
    }
    
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
        return userMapper.toDto(user);
    }
    
    @Override
    public UserDto getUserByPseudo(String pseudo) {
        User user = userRepository.findByPseudo(pseudo)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec le pseudo: " + pseudo));
        return userMapper.toDto(user);
    }
}
