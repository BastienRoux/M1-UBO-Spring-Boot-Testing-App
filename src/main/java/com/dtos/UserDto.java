package com.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String pseudo;
    private String firstName;
    private String lastName;
    private Integer age;
    private String address;
    private String role;
    private LocalDateTime createdAt;
}
