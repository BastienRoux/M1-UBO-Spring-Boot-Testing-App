package com.mappers;

import com.dtos.UserDto;
import com.dtos.UserInputDto;
import com.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setPseudo(user.getPseudo());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAge(user.getAge());
        dto.setAddress(user.getAddress());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        
        return dto;
    }
    
    public User toEntity(UserInputDto dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        user.setPseudo(dto.getPseudo());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setAddress(dto.getAddress());
        user.setPassword(dto.getPassword()); // Note: devrait être hashé en production
        
        return user;
    }
}
